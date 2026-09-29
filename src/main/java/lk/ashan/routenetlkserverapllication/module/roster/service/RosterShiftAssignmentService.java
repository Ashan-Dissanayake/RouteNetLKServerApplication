package lk.ashan.routenetlkserverapllication.module.roster.service;

import ai.timefold.solver.core.api.solver.SolverJob;
import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.roster.event.RosterShiftStatusChangedEvent;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.validation.RosterShiftAssignmentValidator;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterShiftAssignmentService {

    private final RosterShiftAssignmentRepository assignmentRepository;
    private final RosterShiftAssignmentStatusService rosterShiftAssignmentStatusService;
    private final TripRepository tripRepository;
    private final RosterAssignmentMapper rosterAssignmentMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;
    private final RosterShiftAssignmentValidator rosterShiftAssignmentValidator;

    @Qualifier("rosterSolver")
    private final SolverManager<RosterShiftAssignmentSolution, Integer> solverManager;

    @Transactional(readOnly = true)
    public List<RosterShiftAssignmentResponseDto> getAssignmentsByRosterId(Integer rosterId) {
        List<RosterShiftAssignment> assignments = assignmentRepository.findByRosterId(rosterId);
        return rosterAssignmentMapper.toDtoList(assignments);
    }

    @Transactional
    public void generateRosterShiftAssignments(Integer rosterId) {
        // 1. Fetch unassigned slots for this specific Roster
        List<RosterShiftAssignment> entities = assignmentRepository.findUnassignedByRosterId(rosterId);

        if (entities.isEmpty()) {
            log.warn("No unassigned slots found for Roster ID: {}", rosterId);
            return;
        }

        // 2. Fetch Employees with Familiarity from Driver/Conductor tables via Projection
        // Get branchId from the roster associated with these assignments
        Integer branchId = entities.get(0).getRostershift().getRoster().getBranch().getId();

        List<EmployeeFact> employeeFacts = new ArrayList<>();

        List<Driver> drivers = driverRepository.findAvailableDrivers(branchId);

        drivers.forEach(driver -> employeeFacts.add(
                new EmployeeFact(
                        driver.getId(),
                        driver.getEmployee().getFullname(),
                        1,
                        driver.getRoutefamiliaritylevel().getId()
                )
        ));


        List<Conductor> conductors =
                conductorRepository.findAvailableConductors(branchId);

        conductors.forEach(conductor -> employeeFacts.add(
                new EmployeeFact(
                        conductor.getId(),
                        conductor.getEmployee().getFullname(),
                        2,
                        conductor.getRoutefamiliaritylevel().getId()
                )
        ));

        // 3. Prepare Planning Entities and dynamically set Familiarity Requirements
        List<RosterShiftAssignmentPlanning> planningEntities = entities.stream()
                .map(entity -> {
                    RosterShiftAssignmentPlanning planning = rosterAssignmentMapper.toPlanning(entity);

                    // Logic: Determine required skill level based on trips mapped to this shift
                    // If the shift contains Interprovincial trips, level 2 is required.
                    int requiredLevel = determineRequiredFamiliarity(entity.getRostershift());
                    planning.setRequiredFamiliarityLevel(requiredLevel);

                    return planning;
                })
                .toList();

        // 4. Build the Solution object (The Problem Fact)
        RosterShiftAssignmentSolution problem = new RosterShiftAssignmentSolution(
                employeeFacts,
                planningEntities
        );

        log.info("Starting Synchronous Solver for Roster {}", rosterId);

        try {
            // 5. Trigger the Solver and WAIT for completion
            SolverJob<RosterShiftAssignmentSolution, Integer> solverJob =
                    solverManager.solve(rosterId, problem);
            RosterShiftAssignmentSolution finalSolution = solverJob.getFinalBestSolution();

            this.saveResult(finalSolution);

        } catch (InterruptedException | ExecutionException e) {
            log.error("Solver failed for roster {}", rosterId, e);
            throw new RuntimeException("Roster generation failed during optimization", e);
        }
    }

    private int determineRequiredFamiliarity(RosterShift rosterShift) {
        // Use the branch and the shift times to find if any scheduled
        // trips in this slot require high familiarity.
        boolean isHighSkillRequired = tripRepository.existsInterprovincialTripInShift(
                rosterShift.getRoster().getBranch().getId(),
                rosterShift.getShift().getTostart(),
                rosterShift.getShift().getToend()
        );

        return isHighSkillRequired ? 2 : 1;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveResult(RosterShiftAssignmentSolution solution) {
        log.info("--- PERSISTING SOLVER RESULTS ---");

        Integer proposedStatusId = rosterShiftAssignmentStatusService.getByName("Proposed").getId();

        for (RosterShiftAssignmentPlanning planning : solution.getAssignmentList()) {
            if (planning.getEmployeeFact() != null) {
                // Update the database using a single optimized query
                assignmentRepository.updateEmployeeAndStatusDirectly(
                        planning.getId(), // This is the rostershiftassignment primary key
                        planning.getEmployeeFact().getId(),
                        proposedStatusId
                );
            }
        }
        log.info("--- SOLVER PERSISTENCE COMPLETE ---");
    }

    @Transactional
    public void approveSuggestion(Integer assignmentId) {

        RosterShiftAssignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Roster shift assignment not found: "
                                                + assignmentId
                                ));

        RosterShiftAssignmentStatus currentStatus =
                assignment.getRostershiftassignmentstatus();

        RosterShiftAssignmentStatus confirmedStatus =
                rosterShiftAssignmentStatusService.getByName("Confirmed");

        changeStatusAndPublishEvent(
                assignment,
                currentStatus,
                confirmedStatus
        );

        log.info(
                "Roster shift assignment {} approved successfully",
                assignmentId
        );
    }

    @Transactional
    public void cancelSuggestion(Integer assignmentId) {

        RosterShiftAssignment assignment =
                assignmentRepository.findById(assignmentId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Roster shift assignment not found: "
                                                + assignmentId
                                ));

        RosterShiftAssignmentStatus currentStatus =
                assignment.getRostershiftassignmentstatus();


        RosterShiftAssignmentStatus canceledStatus =
                rosterShiftAssignmentStatusService.getByName("Canceled");


        changeStatusAndPublishEvent(
                assignment,
                currentStatus,
                canceledStatus
        );

        log.info(
                "Roster shift assignment {} canceled successfully",
                assignmentId
        );
    }

    private String determineCrewType(Integer employeeId) {

        if (driverRepository.existsByEmployee_Id(employeeId)) {
            return "DRIVER";
        }

        if (conductorRepository.existsByEmployee_Id(employeeId)) {
            return "CONDUCTOR";
        }

        throw new BusinessRuleViolationException(
                "Employee is not assigned as a Driver or Conductor."
        );
    }

    private void changeStatusAndPublishEvent(
            RosterShiftAssignment assignment,
            RosterShiftAssignmentStatus currentStatus,
            RosterShiftAssignmentStatus targetStatus
    ) {

        rosterShiftAssignmentValidator.validateStatusTransition(
                currentStatus,
                targetStatus
        );

        assignment.setRostershiftassignmentstatus(targetStatus);

        assignmentRepository.save(assignment);

        String crewType =
                determineCrewType(
                        assignment.getEmployee().getId()
                );

        applicationEventPublisher.publishEvent(
                new RosterShiftStatusChangedEvent(
                        assignment.getId(),
                        assignment.getEmployee().getId(),
                        assignment.getRostershift()
                                .getRoster()
                                .getBranch()
                                .getId(),
                        crewType,
                        currentStatus.getName(),
                        targetStatus.getName()
                )
        );

        log.info(
                "Roster shift assignment {} status changed from {} to {}",
                assignment.getId(),
                currentStatus.getName(),
                targetStatus.getName()
        );
    }

}
