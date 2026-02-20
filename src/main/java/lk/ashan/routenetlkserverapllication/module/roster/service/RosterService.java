package lk.ashan.routenetlkserverapllication.module.roster.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftRosterAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterMapper;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.ShiftRosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.*;
import lk.ashan.routenetlkserverapllication.module.roster.panner.RosterAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.panner.RosterAssignmentSolverService;
import lk.ashan.routenetlkserverapllication.module.roster.panner.RosterScheduleSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.*;
import lk.ashan.routenetlkserverapllication.module.roster.state.roster.RosterState;
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
    private final ShiftRosterAssignmentStatusRepository shiftRosterAssignmentStatusRepository;

    private final RosterRepository rosterRepository;
    private final RosterStatusRepository rosterStatusRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final RoleRepository roleRepository;
    private final ShiftRosterAssignmentRepository shiftRosterAssignmentRepository;

    private final RosterMapper rosterMapper;
    private final ShiftRosterAssignmentMapper shiftRosterAssignmentMapper;

    private final RosterContextBuilder rosterContextBuilder;
    private final RosterStatusFactory rosterStatusFactory;
    private final List<RosterCreationStrategy> validationStrategies;

    private final RosterAssignmentSolverService solverService;

    private final RosterAssignmentPreValidationStrategy preValidationStrategy;
    private final AssignmentApprovalValidationStrategy approvalValidationStrategy;
    private final AssignmentRejectionValidationStrategy rejectionValidationStrategy;
    private final AssignmentClearAllValidationStrategy clearAllValidationStrategy;


    public List<RosterDetailResponseDto> getRosters(){
        return rosterMapper.toDtoList(rosterRepository.findAll());
    }

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

    @Transactional
    public RosterAssignmentSuggestionResponse rosterAssigmentSuggestion(Integer rosterId) {

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

    @Transactional
    public ShiftRosterAssignmentDto approveSuggestion(Integer assignmentId) {

        approvalValidationStrategy.validate(assignmentId);

        Shiftrosterassignment assignment = shiftRosterAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assignment not found"
                ));

        // Validate it's a suggestion
        if (!"SUGGESTED".equalsIgnoreCase(
                assignment.getShiftrosterassignmentstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only approve SUGGESTED assignments. " +
                            "Current status: " + assignment.getShiftrosterassignmentstatus().getName()
            );
        }

        // Validate roster is still DRAFT
        if (!"DRAFT".equalsIgnoreCase(
                assignment.getRoster().getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only approve suggestions for DRAFT rosters"
            );
        }

        // Keep status as SUGGESTED (will be CONFIRMED later when roster is LOCKED)
        // Just mark as approved by user
        log.info("Approved suggestion {}", assignmentId);

        return shiftRosterAssignmentMapper.toDto(assignment);
    }

    @Transactional
    public void rejectSuggestion(Integer assignmentId) {

        rejectionValidationStrategy.validate(assignmentId);

        Shiftrosterassignment assignment = shiftRosterAssignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assignment not found"
                ));

        // Validate it's a suggestion
        if (!"SUGGESTED".equalsIgnoreCase(
                assignment.getShiftrosterassignmentstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only reject SUGGESTED assignments"
            );
        }

        // Validate roster is still DRAFT
        if (!"DRAFT".equalsIgnoreCase(
                assignment.getRoster().getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only reject suggestions for DRAFT rosters"
            );
        }

        // Delete the suggestion
        shiftRosterAssignmentRepository.delete(assignment);

        log.info("Rejected and deleted suggestion {}", assignmentId);
    }

    @Transactional
    public void clearAllSuggestions(Integer rosterId) {

        clearAllValidationStrategy.validate(rosterId);

        Roster roster = rosterRepository.findById(rosterId)
                .orElseThrow(() -> new ResourceNotFoundException("Roster not found"));

        // Guard: Can only clear for DRAFT rosters
        if (!"DRAFT".equalsIgnoreCase(roster.getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only clear suggestions for DRAFT rosters"
            );
        }

        List<Shiftrosterassignment> suggestions = shiftRosterAssignmentRepository
                .findByRoster_IdAndShiftrosterassignmentstatus_Name(rosterId, "Suggested");

        shiftRosterAssignmentRepository.deleteAll(suggestions);

        log.info("Cleared {} suggestions for roster {}",
                suggestions.size(), rosterId);
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
