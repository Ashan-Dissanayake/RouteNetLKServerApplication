package lk.ashan.routenetlkserverapllication.module.roster.service;

import ai.timefold.solver.core.api.solver.SolverManager;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.mapper.RosterAssignmentMapper;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.EligibleCrewDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.planner.EmployeeFact;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentPlanning;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterShiftAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RosterShiftAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment.RosterShiftAssignmentStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RosterShiftAssignmentService {

    private final RosterShiftAssignmentRepository assignmentRepository;
    private final RosterShiftAssignmentStatusService rosterShiftAssignmentStatusService;
    private final EmployeeRepository employeeRepository;
    private final TripRepository tripRepository;
    private final RosterShiftAssignmentRepository rosterShiftAssignmentRepository;
    private final RosterAssignmentMapper rosterAssignmentMapper;


    @Qualifier("rosterSolver")
    private final SolverManager<RosterShiftAssignmentSolution, Integer> solverManager;
    private final RosterShiftAssignmentStateTransitionHandler rosterShiftAssignmentStateTransitionHandler;

    @Transactional(readOnly = true)
    public List<RosterShiftAssignmentResponseDto> getAssignmentsByRosterId(Integer rosterId) {
        List<RosterShiftAssignment> assignments = assignmentRepository.findByRosterId(rosterId);
        return rosterAssignmentMapper.toDtoList(assignments);
    }

    /**
     * Finds all rostered crew members who are:
     * 1. Not busy during the trip time
     * 2. Covering the trip's duration in their shift
     * 3. Skilled enough (Familiarity Check)
     */
//    public List<EligibleCrewDto> getEligibleCrewForTrip(Integer tripId, Integer designationId) {
//        Trip trip = tripRepository.findById(tripId)
//                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
//
//        // 1. Fetch from Repository (Time & Designation check)
//        List<RosterShiftAssignment> potentialCrew = rosterShiftAssignmentRepository.findAvailableCrewForTrip(
//                trip.getDoservice(),
//                designationId,
//                trip.getTodepature(),
//                trip.getToarrival()
//        );
//
//        // 2. Filter by Skill & Map using MapStruct
//        Integer requiredLevel = trip.getPermit().getRoute().getRoutetype().getId();
//
//        List<RosterShiftAssignment> filteredCrew = potentialCrew.stream()
//                .filter(rsa -> rsa.getEffectiveFamiliarity() >= requiredLevel)
//                .toList();
//
//        return rosterAssignmentMapper.toEligibleDtoList(filteredCrew);
//    }

    @Transactional
    public void generateRoster(Integer rosterId) {
        List<RosterShiftAssignment> entities = assignmentRepository.findUnassignedByRosterId(rosterId);
        if (entities.isEmpty()) return;

        // Get the date for the roster (assuming all assignments in a roster are for the same day)
        LocalDate rosterDate = entities.get(0).getRostershift().getDoshift();

        // 1. Prepare Demand: Tag the RosterShift entities with familiarity requirements
        // Extract unique RosterShifts from the assignments
        List<RosterShift> uniqueShifts = entities.stream()
                .map(RosterShiftAssignment::getRostershift)
                .distinct()
                .toList();

        //prepareRosterDemand(rosterDate, uniqueShifts);

        List<Integer> requiredIds = List.of(1, 2);
        List<Employee> employeeEntities = employeeRepository.findActiveEmployeesByDesignations(requiredIds);

        List<EmployeeFact> employeeFacts = employeeEntities.stream()
                .filter(e -> e.getBranch().getId() == 1)
                .map(rosterAssignmentMapper::toFact) // Mapper should now include familiarity
                .toList();

        // 2. Map to Planning: Ensure mapper copies 'requiredFamiliarityLevel' from entity to planning
        List<RosterShiftAssignmentPlanning> planningEntities = entities.stream()
                .map(rosterAssignmentMapper::toPlanning)
                .toList();

        RosterShiftAssignmentSolution problem = new RosterShiftAssignmentSolution(
                employeeFacts,
                planningEntities
        );

        log.info("Employees passed to solver: " + employeeFacts.size());
        for(EmployeeFact f : employeeFacts) {
            log.info("Emp: " + f.getFullname() + " | Desig: " + f.getDesignationId());
        }

        for(RosterShiftAssignmentPlanning p : planningEntities) {
            log.info("Shift ID: " + p.getId() + " | Needs Desig: " + p.getDesignationId());
        }

        solverManager.solve(rosterId, (Integer id) -> problem, this::saveResult);
    }

//    public void prepareRosterDemand(LocalDate date, List<RosterShift> rosterShifts) {
//        // 1. Get all Interprovincial trips for the day
//        List<Trip> interprovincialTrips = tripRepository.findInterprovincialTrips(date);
//
//        for (RosterShift rs : rosterShifts) {
//            // 2. Check if this shift covers ANY interprovincial trip
//            boolean coversInterprovincial = interprovincialTrips.stream()
//                    .anyMatch(t -> isTripInShift(t, rs.getShift()));
//
//            // 3. Flag the shift so Timefold knows who to put there
//            if (coversInterprovincial) {
//                rs.setRequiredFamiliarityLevel(2); // 2 = Medium/High
//            } else {
//                rs.setRequiredFamiliarityLevel(1); // 1 = Low/Local
//            }
//        }
//    }

    private boolean isTripInShift(Trip trip, Shift shift) {
        // A trip "belongs" to a shift if it starts within the shift's duration
        LocalTime tripStart = trip.getTodepature();
        LocalTime shiftStart = shift.getTostart();
        LocalTime shiftEnd = shift.getToend();

        // Standard check (handle shifts that cross midnight if necessary)
        if (shiftEnd.isAfter(shiftStart)) {
            return !tripStart.isBefore(shiftStart) && tripStart.isBefore(shiftEnd);
        } else {
            // Handle night shifts (e.g., 22:00 to 06:00)
            return !tripStart.isBefore(shiftStart) || tripStart.isBefore(shiftEnd);
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveResult(RosterShiftAssignmentSolution solution) {
        log.info("--- PERSISTENCE START (DIRECT UPDATE) ---");

        // 1. Get the Proposed Status ID (Assuming it's 2, but fetching dynamically is safer)
        Integer proposedStatusId = rosterShiftAssignmentStatusService.getByName("Proposed").getId();

        for (RosterShiftAssignmentPlanning planning : solution.getAssignmentList()) {
            if (planning.getEmployeeFact() != null) {

                // 2. Call the new combined update method
                assignmentRepository.updateEmployeeAndStatusDirectly(
                        planning.getId(),
                        planning.getEmployeeFact().getId(),
                        proposedStatusId
                );

                log.info("SUCCESS: Assignment {} set to Employee {} with status PROPOSED",
                        planning.getId(), planning.getEmployeeFact().getId());
            }
        }
        log.info("--- PERSISTENCE COMPLETE ---");
    }


    @Transactional
    public void approveSuggestion(Integer assignmentId) {
        RosterShiftAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        RosterShiftAssignmentStatus confirmedStatus =
                rosterShiftAssignmentStatusService.getByName("Confirmed");

        rosterShiftAssignmentStateTransitionHandler.transitionTo(assignment, confirmedStatus);

        assignmentRepository.save(assignment);
        log.info("Manager confirmed assignment ID: {} for Employee: {}",
                assignment.getId(), assignment.getEmployee().getFullname());
    }

    @Transactional
    public void cancelSuggestion(Integer assignmentId) {
        RosterShiftAssignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Assignment not found with ID: " + assignmentId));

        RosterShiftAssignmentStatus canceledStatus =
                rosterShiftAssignmentStatusService.getByName("Canceled");

        rosterShiftAssignmentStateTransitionHandler.transitionTo(assignment, canceledStatus);

        assignmentRepository.save(assignment);

        log.info("Manager cancelled assignment ID: {} for Employee: {}",
                assignment.getId(), assignment.getEmployee().getFullname());
    }

}
