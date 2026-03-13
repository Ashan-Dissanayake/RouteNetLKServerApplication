package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.dto.*;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftRosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.entity.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.*;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentSolverService;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterScheduleSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.*;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterState;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterStatusFactory;
import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterContextBuilder;
import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterCreationContext;
import lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy.*;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableSoftDeleteFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterService {

    private final RosterRepository rosterRepository;
    private final RosterStatusRepository rosterStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final RoleRepository roleRepository;
    private final ShiftRosterAssignmentRepository shiftRosterAssignmentRepository;
    private final ShiftRosterAssignmentStatusRepository shiftRosterAssignmentStatusRepository;

    private final RosterMapper rosterMapper;
    private final ShiftRosterAssignmentMapper shiftRosterAssignmentMapper;

    private final RosterContextBuilder rosterContextBuilder;
    private final RosterStatusFactory rosterStatusFactory;
    private final RosterStateTransitionHandler rosterTransitionHandler;
    private final List<RosterCreationStrategy> validationStrategies;

    private final RosterAssignmentSolverService solverService;

    private final RosterAssignmentPreValidationStrategy preValidationStrategy;
    private final AssignmentApprovalValidationStrategy approvalValidationStrategy;
    private final AssignmentRejectionValidationStrategy rejectionValidationStrategy;
    private final AssignmentClearAllValidationStrategy clearAllValidationStrategy;


    @Transactional(readOnly = true)
    public List<RosterDetailResponseDto> getRosters(){
        return rosterMapper.toDtoList(rosterRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<RosterDetailResponseDto> searchRosters(@NotNull HashMap<String, String> params) {

        List<Roster> rosters = rosterRepository.findAll();

            String branchName = params.get("ssname");
            String rosterStatusId= params.get("ssrosterstatus");

            Stream<Roster> rosterStream = rosters.stream();

            if(branchName!=null)rosterStream = rosterStream.filter(r->r.getBranch().getName().toLowerCase().contains(branchName.toLowerCase()));
            if(rosterStatusId!=null)rosterStream = rosterStream.filter(r->r.getRosterstatus().getId()==Integer.parseInt(rosterStatusId));

            return rosterMapper.toDtoList( rosterStream.collect(Collectors.toList()));
    }

    @Transactional
    @DisableSoftDeleteFilter
    public RosterDetailResponseDto createRoster(@NotNull RosterCreateRequestDto createRequestDto){

        RosterCreationContext creationContext = rosterContextBuilder.buildForCreate(createRequestDto);
        validationStrategies.forEach(strategy -> strategy.validate(creationContext));

        Roster roster = rosterMapper.toEntity(createRequestDto);

        Rosterstatus draftStatus = rosterStatusRepository
                .findByName("Draft")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster status not found: Draft"
                ));


        RosterState state = rosterStatusFactory.getState(draftStatus.getName());
        state.validateInitial();



        roster.setRosterstatus(draftStatus);

        Roster savedRoster = rosterRepository.save(roster);

        return rosterMapper.toDto(rosterRepository.save(savedRoster));
    }

    /**
     * Lock roster (DRAFT → LOCKED)
     * Employees can now confirm/reject assignments
     */
    @Transactional
    public RosterDetailResponseDto lockRoster(Integer rosterId) {

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException("Roster not found"));

        Rosterstatus lockedStatus = rosterStatusRepository
                .findByName("Locked")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster status not found: Locked"
                ));

        // Use transition handler (validates and executes side effects)
        rosterTransitionHandler.transitionTo(roster, lockedStatus);

        Roster savedRoster = rosterRepository.save(roster);

        log.info("Locked roster {} - employees can now confirm assignments",
                savedRoster.getId());

        return rosterMapper.toDto(savedRoster);
    }

    /**
     * Unlock roster (LOCKED → DRAFT)
     * Resets all CONFIRMED assignments back to SUGGESTED
     */
    @Transactional
    public RosterDetailResponseDto unlockRoster(Integer rosterId) {

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException("Roster not found"));

        Rosterstatus draftStatus = rosterStatusRepository
                .findByName("Draft")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster status not found: Draft"
                ));

        // Use transition handler (automatically resets confirmations)
        rosterTransitionHandler.transitionTo(roster, draftStatus);

        Roster savedRoster = rosterRepository.save(roster);

        log.info("Unlocked roster {} back to DRAFT - confirmations reset to SUGGESTED",
                savedRoster.getId());

        return rosterMapper.toDto(savedRoster);
    }

    /**
     * Archive roster (LOCKED → ARCHIVED)
     * Final state, no further modifications allowed
     */
    @Transactional
    public RosterDetailResponseDto archiveRoster(Integer rosterId) {

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException("Roster not found"));

        Rosterstatus archivedStatus = rosterStatusRepository
                .findByName("Archived")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster status not found: Archived"
                ));

        // Use transition handler (validates all assignments confirmed)
        rosterTransitionHandler.transitionTo(roster, archivedStatus);

        Roster savedRoster = rosterRepository.save(roster);

        log.info("Archived roster {} - now read-only", savedRoster.getId());

        return rosterMapper.toDto(savedRoster);
    }

    @Transactional
    public RosterAssignmentSuggestionResponse rosterAssignmentSuggestion(Integer rosterId) {

        log.info("========== GENERATING SUGGESTIONS FOR ROSTER {} ==========", rosterId);


        //1. Run pre-validation (roster exists, is DRAFT, etc.)
        RosterCreationContext validationContext = RosterCreationContext.builder()
                .currentRosterId(rosterId)
                .build();

        preValidationStrategy.validate(validationContext);

        // 1. Load and validate roster
        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException("Roster not found"));

        // Guard: Can only generate for DRAFT rosters
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only generate suggestions for DRAFT rosters. " +
                            "Current status: " + roster.getRosterstatus().getName()
            );
        }

        // 2. Load problem data
        log.info("Loading problem data for branch {}", roster.getBranch().getId());

        // Get all active shifts for this branch
        List<Shift> shifts = shiftRepository
                .findByBranch_IdAndShiftstatus_Name(
                        roster.getBranch().getId(),
                        "Active"
                );

        if (shifts.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "No active shifts found for branch " + roster.getBranch().getName()
            );
        }

        log.info("Found {} active shifts", shifts.size());

        // Get all roles that need to be filled
        List<Role> roles = roleRepository.findAll();

        if (roles.isEmpty()) {
            throw new BusinessRuleViolationException("No roles defined in system");
        }

        log.info("Found {} roles to assign", roles.size());

        // Get all available employees for this branch
        List<Employee> candidateEmployees = employeeRepository
                .findByBranch_IdAndEmployeestatus_NameAndDeletedFalse(
                        roster.getBranch().getId(),
                        "Active"
                );

        if (candidateEmployees.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "No active employees available for branch " + roster.getBranch().getName()
            );
        }

        log.info("Found {} candidate employees", candidateEmployees.size());

        // Get existing confirmed assignments (constraints)
        List<Shiftrosterassignment> existingAssignments =
                shiftRosterAssignmentRepository.findByRoster_Id(rosterId);

        log.info("Found {} existing assignments", existingAssignments.size());


        // 3. Run OptaPlanner solver
        RosterScheduleSolution solved = solverService.solve(
                roster,
                shifts,
                roles,
                candidateEmployees,
                existingAssignments
        );

        // 4. Check if solution is feasible
        if (!solved.isFeasible()) {
            log.warn("⚠️ Solution has hard constraint violations: {}",
                    solved.getScore());
            throw new BusinessRuleViolationException(
                    "Could not generate feasible solution. " +
                            "Score: " + solved.getScore() + ". " +
                            "Try adjusting constraints or adding more employees."
            );
        }

        log.info("✅ Feasible solution found with score: {}", solved.getScore());

        // 5. Clear any existing SUGGESTED assignments
        List<Shiftrosterassignment> oldSuggestions = existingAssignments.stream()
                .filter(a -> "SUGGESTED".equalsIgnoreCase(
                        a.getShiftrosterassignmentstatus().getName()))
                .collect(Collectors.toList());

        if (!oldSuggestions.isEmpty()) {
            log.info("Removing {} old suggestions", oldSuggestions.size());
            shiftRosterAssignmentRepository.deleteAll(oldSuggestions);
        }

        // 6. Convert planning entities to database entities
        Shiftrosterassignmentstatus suggestedStatus = shiftRosterAssignmentStatusRepository
                .findByName("Suggested")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assignment status not found: Suggested"
                ));

        List<Shiftrosterassignment> newSuggestions = solved.getAssignments().stream()
                .filter(planning -> planning.getAssignedEmployee() != null) // Only assigned ones
                .map(planning -> convertToEntity(planning, roster, suggestedStatus))
                .collect(Collectors.toList());

        log.info("Created {} new assignment suggestions", newSuggestions.size());

        log.info("Created {} new assignment suggestions", newSuggestions.size());

        // 7. Save all suggestions
        List<Shiftrosterassignment> savedAssignments =
                shiftRosterAssignmentRepository.saveAll(newSuggestions);

        // 8. Build summary response
        long unfilledCount = solved.getAssignments().stream()
                .filter(a -> a.getAssignedEmployee() == null)
                .count();

        RosterAssignmentSuggestionResponse summary = RosterAssignmentSuggestionResponse.builder()
                .rosterId(rosterId)
                .totalAssignmentsNeeded(solved.getAssignmentCount())
                .assignmentsFilled(newSuggestions.size())
                .assignmentsUnfilled((int) unfilledCount)
                .score(solved.getScore().toString())
                .feasible(true)
                .build();

        log.info("========== SUGGESTIONS GENERATION COMPLETE ==========");

        return summary;

    }

    @Transactional(readOnly = true)
    public List<RosterAssignmentSuggestionResponse> getSuggestions(Integer rosterId) {

        List<Shiftrosterassignment> suggestions = shiftRosterAssignmentRepository
                .findByRoster_IdAndShiftrosterassignmentstatus_Name(rosterId, "Suggested");

        return shiftRosterAssignmentMapper.toDtoList(suggestions);
    }

    /**
     * Approve a suggested assignment (keep it for final roster)
     */
    @Transactional
    public ShiftRosterAssignmentDto approveSuggestion(Integer assignmentId) {

        //Run approval validation
        approvalValidationStrategy.validate(assignmentId);

        //No duplicate validation - trust the strategy
        Shiftrosterassignment assignment = shiftRosterAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        log.info("Approved suggestion {}", assignmentId);

        return shiftRosterAssignmentMapper.toDto(assignment);
    }

    /**
     * Reject a suggested assignment (delete it)
     */
    @Transactional
    public void rejectSuggestion(Integer assignmentId) {

        //Run rejection validation
        rejectionValidationStrategy.validate(assignmentId);

        //No duplicate validation - trust the strategy
        Shiftrosterassignment assignment = shiftRosterAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found"));

        shiftRosterAssignmentRepository.delete(assignment);

        log.info("Rejected and deleted suggestion {}", assignmentId);
    }

    /**
     * Clear all suggestions for a roster
     */
    @Transactional
    public void clearAllSuggestions(Integer rosterId) {

        //Run clear validation
        clearAllValidationStrategy.validate(rosterId);

        //No duplicate validation - trust the strategy
        List<Shiftrosterassignment> suggestions = shiftRosterAssignmentRepository
                .findByRoster_IdAndShiftrosterassignmentstatus_Name(rosterId, "Suggested");

        shiftRosterAssignmentRepository.deleteAll(suggestions);

        log.info("Cleared {} suggestions for roster {}", suggestions.size(), rosterId);
    }


    /**
     * Update existing roster
     * Can only update when roster is in DRAFT status
     * Branch cannot be changed
     */
    @Transactional
    @DisableSoftDeleteFilter
    public RosterDetailResponseDto updateRoster(@NotNull RosterUpdateRequestDto updateRequestDto) {

        // Load existing roster
        Roster roster = rosterRepository.findById(updateRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster not found with ID: " + updateRequestDto.getId()
                ));

        // Guard: Can only update DRAFT rosters
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Cannot update roster. Only DRAFT rosters can be edited. " +
                            "Current status: " + roster.getRosterstatus().getName()
            );
        }

        // Run validation strategies for the updated dates
        RosterCreationContext validationContext = rosterContextBuilder.buildForUpdate(updateRequestDto);
        validationStrategies.forEach(strategy -> strategy.validate(validationContext));

        // Update fields (only dates can be changed, branch is immutable)
        roster.setDostartofweek(updateRequestDto.getDostartofweek());
        roster.setDoendofweek(updateRequestDto.getDoendofweek());

        Roster updatedRoster = rosterRepository.save(roster);

        log.info("Updated roster {} - new week: {} to {}",
                updatedRoster.getId(),
                updatedRoster.getDostartofweek(),
                updatedRoster.getDoendofweek()
        );

        return rosterMapper.toDto(updatedRoster);
    }

    /**
     * Soft delete roster
     * Can only delete DRAFT rosters
     */
    @Transactional
    public void deleteRoster(Integer rosterId) {

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Roster not found with ID: " + rosterId
                ));

        // Guard: Can only delete DRAFT rosters
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Cannot delete roster. Only DRAFT rosters can be deleted. " +
                            "Current status: " + roster.getRosterstatus().getName()
            );
        }

        // Soft delete
        roster.setDeleted(true);
        rosterRepository.save(roster);

        log.info("Soft deleted roster {}", rosterId);
    }

    /**
     * Regenerate suggestions (clear old + generate new)
     * Convenience method that combines clear and generate
     */
    @Transactional
    public RosterAssignmentSuggestionResponse regenerateSuggestions(Integer rosterId) {

        log.info("Regenerating suggestions for roster {}", rosterId);

        // Clear old suggestions
        clearAllSuggestions(rosterId);

        // Generate new suggestions
        RosterAssignmentSuggestionResponse response = rosterAssignmentSuggestion(rosterId);

        log.info("Successfully regenerated {} suggestions for roster {}",
                response.getAssignmentsFilled(), rosterId);

        return response;
    }


    private Shiftrosterassignment convertToEntity(
            RosterAssignmentPlanning planning,
            Roster roster,
            Shiftrosterassignmentstatus status) {

        // Find the actual employee entity
        Employee employee = employeeRepository
                .findById(planning.getAssignedEmployee().getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Employee not found: " + planning.getAssignedEmployee().getId()
                ));

        Shiftrosterassignment assignment = new Shiftrosterassignment();
        assignment.setRoster(roster);
        assignment.setShift(planning.getShift());
        assignment.setRole(planning.getRole());
        assignment.setEmployee(employee);
        assignment.setDoassigned(planning.getDoassigned());
        assignment.setShiftrosterassignmentstatus(status);

        return assignment;
    }
}
