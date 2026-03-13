package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Role;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shift;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.repository.RoleRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationSuggestionResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.event.CrewAllocationConfirmedEvent;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.mapper.TripCrewAllocationMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner.TripCrewAllocationSolverService;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.planner.TripCrewScheduleSolution;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository.TripAllocationStatusRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.repository.TripCrewAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state.TripCrewAllocationState;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state.TripCrewAllocationStateFactory;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripCrewAllocationService {

    private final TripCrewAllocationRepository tripCrewAllocationRepository;
    private final TripCrewAllocationMapper tripCrewAllocationMapper;
    private final TripRepository tripRepository;
    private final ShiftDeterminationService shiftDeterminationService;
    private final ShiftRosterAssignmentRepository shiftRosterAssignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final TripCrewAllocationSolverService solverService;
    private final TripAllocationStatusRepository tripAllocationStatusRepository;
    private final TripCrewAllocationStateFactory tripCrewAllocationStateFactory;
    private ApplicationEventPublisher eventPublisher;



    @Transactional(readOnly = true)
    public List<TripCrewAllocationDetailResponseDto> getTripAllocations() {
        return tripCrewAllocationMapper.toDtoList(tripCrewAllocationRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TripCrewAllocationDetailResponseDto> searchTripCrewAllocations(@NotNull HashMap<String, String> params) {

        List<Tripcrewallocation> tripCrewAllocations = tripCrewAllocationRepository.findAll();

        if (!params.isEmpty()) {

            String tripId = params.get("sstripid");
            String tripAllocationStatusId = params.get("sstripallocationstatusid");

            Stream<Tripcrewallocation> tripcrewallocationStream = tripCrewAllocations.stream();

            if (tripId != null)
                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTrip().getId() == Integer.parseInt(tripId));
            if (tripAllocationStatusId != null)
                tripcrewallocationStream = tripcrewallocationStream.filter(t -> t.getTripallocationstatus().getId() == Integer.parseInt(tripAllocationStatusId));

            return tripCrewAllocationMapper.toDtoList(tripcrewallocationStream.collect(Collectors.toList()));
        }

        return tripCrewAllocationMapper.toDtoList(tripCrewAllocations);
    }


    /**
     * Generate crew allocation suggestions using OptaPlanner.
     *
     * Flow:
     * 1. Load trip and derive shift from departure time
     * 2. Load confirmed roster for that shift/date
     * 3. Run OptaPlanner to suggest optimal assignments
     * 4. Save suggestions with SUGGESTED status
     */
    @Transactional
    public TripCrewAllocationSuggestionResponseDto generateSuggestions(Integer tripId) {
        log.info("========== GENERATE CREW SUGGESTIONS START ==========");
        log.info("Trip ID: {}", tripId);

        // 1. Load trip
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripId));

        log.info("Trip: Service date={}, Departure={}, Route={}",
                trip.getDoservice(), trip.getTodepature(), trip.getPermite().getRoute().getNumber());

        // 2. Determine shift from departure time
        Shift derivedShift = shiftDeterminationService.determineShiftForTrip(
                trip.getTodepature(),
                trip.getBranch().getId()
        );

        log.info("Derived shift: {} ({} - {})",
                derivedShift.getName(), derivedShift.getTostart(), derivedShift.getToend());

        // 3. Load confirmed roster for this shift/date
        List<Shiftrosterassignment> confirmedRoster = shiftRosterAssignmentRepository
                .findByShift_IdAndDoassignedAndShiftrosterassignmentstatus_Name(
                        derivedShift.getId(),
                        trip.getDoservice(),
                        "Confirmed"
                );

        log.info("Found {} confirmed roster assignments", confirmedRoster.size());

        if (confirmedRoster.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "No confirmed roster assignments found for shift " + derivedShift.getName() +
                            " on date " + trip.getDoservice()
            );
        }

        // 4. Extract candidate employees (with eager loading)
        List<Integer> employeeIds = confirmedRoster.stream()
                .map(ra -> ra.getEmployee().getId())
                .distinct()
                .collect(Collectors.toList());

        List<Employee> candidates = employeeRepository.findByIdInWithCrewData(employeeIds);

        log.info("Loaded {} candidate employees with crew qualifications", candidates.size());

        // 5. Load roles
        Role driverRole = roleRepository.findByName("Driver")
                .orElseThrow(() -> new ResourceNotFoundException("Driver role not found"));
        Role conductorRole = roleRepository.findByName("Conductor")
                .orElseThrow(() -> new ResourceNotFoundException("Conductor role not found"));
        List<Role> roles = List.of(driverRole, conductorRole);

        // 6. Load existing allocations (to prevent double-booking)
        List<Tripcrewallocation> existingAllocations =
                tripCrewAllocationRepository.findByTrip_DoserviceAndTripallocationstatus_NameIn(
                        trip.getDoservice(),
                        List.of("Suggested", "Confirmed")
                );

        log.info("Found {} existing allocations for this date", existingAllocations.size());

        // 7. RUN OPTAPLANNER SOLVER
        TripCrewScheduleSolution solution = solverService.solve(
                trip,
                derivedShift,
                roles,
                candidates,
                confirmedRoster,
                existingAllocations
        );

        log.info("Solver completed: Score={}, Feasible={}",
                solution.getScore(), solution.isFeasible());

        // 8. Check if feasible
        if (!solution.isFeasible()) {
            String report = solverService.getConstraintViolationReport(solution);
            log.error("Infeasible solution:\n{}", report);
            throw new BusinessRuleViolationException(
                    "Could not generate feasible crew allocation. Score: " + solution.getScore()
            );
        }

        // 9. Save suggestions to database
        List<TripCrewAllocationDetailResponseDto> savedAllocations =
                saveSuggestions(solution, trip, derivedShift);

        // 10. Build response
        TripCrewAllocationSuggestionResponseDto response = TripCrewAllocationSuggestionResponseDto.builder()
                .tripId(trip.getId())
                .doservice(trip.getDoservice())
                .todepature(trip.getTodepature())
                .feasible(solution.isFeasible())
                .score(solution.getScore().toString())
                .suggestedAllocations(savedAllocations)
                .assignmentsFilled((int) solution.getFilledCount())
                .assignmentsUnfilled((int) solution.getUnfilledCount())
                .build();

        log.info("========== GENERATE CREW SUGGESTIONS END ==========");
        return response;
    }

    /**
     * Save OptaPlanner suggestions to database with SUGGESTED status.
     */
    private List<TripCrewAllocationDetailResponseDto> saveSuggestions(
            TripCrewScheduleSolution solution,
            Trip trip,
            Shift shift) {

        Tripallocationstatus suggestedStatus = tripAllocationStatusRepository.findByName("Suggested")
                .orElseThrow(() -> new ResourceNotFoundException("Status 'Suggested' not found"));

        return solution.getFilledAssignments().stream()
                .map(assignment -> {
                    // Find employee entity
                    Employee employee = employeeRepository
                            .findById(assignment.getAssignedEmployeeId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Employee not found: " + assignment.getAssignedEmployeeId()));

                    // Create allocation entity
                    Tripcrewallocation allocation = Tripcrewallocation.builder()
                            .trip(trip)
                            .employee(employee)
                            .role(assignment.getRole())
                            .derivedshift(shift)
                            .tripallocationstatus(suggestedStatus)
                            .toallocated(LocalTime.now())
                            .build();

                    Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);

                    log.info("Saved suggestion: Trip={}, Employee={}, Role={}",
                            trip.getId(), employee.getNumber(), assignment.getRole().getName());

                    return tripCrewAllocationMapper.toDto(saved);
                })
                .collect(Collectors.toList());
    }

    /**
     * Approve a suggested allocation.
     * State Transition: SUGGESTED → CONFIRMED
     * Uses State Pattern for validation.
     */
    @Transactional
    public TripCrewAllocationDetailResponseDto approveSuggestion(Integer allocationId) {
        log.info("Approving allocation: {}", allocationId);

        Tripcrewallocation allocation = tripCrewAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + allocationId));

        // Get current state
        TripCrewAllocationState currentState = tripCrewAllocationStateFactory.getState(
                allocation.getTripallocationstatus().getName()
        );

        // Get target status
        Tripallocationstatus confirmedStatus = tripAllocationStatusRepository.findByName("Confirmed")
                .orElseThrow(() -> new ResourceNotFoundException("Status 'Confirmed' not found"));

        // Validate transition using State Pattern
        currentState.transitionTo(allocation, confirmedStatus);

        // Save
        Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);
        eventPublisher.publishEvent(
                new CrewAllocationConfirmedEvent(
                        saved.getTrip().getId(),
                        saved.getRole().getId(),
                        saved.getEmployee().getId()
                )
        );
        log.info("Allocation approved: {} - {} for trip {}",
                saved.getEmployee().getNumber(), saved.getRole().getName(), saved.getTrip().getId());

        return tripCrewAllocationMapper.toDto(saved);
    }

    /**
     * Reject a suggested allocation.
     * State Transition: SUGGESTED → REJECTED
     * Uses State Pattern for validation.
     */
    @Transactional
    public TripCrewAllocationDetailResponseDto rejectSuggestion(Integer allocationId) {
        log.info("Rejecting allocation: {}", allocationId);

        Tripcrewallocation allocation = tripCrewAllocationRepository.findById(allocationId)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found: " + allocationId));

        // Get current state
        TripCrewAllocationState currentState = tripCrewAllocationStateFactory.getState(
                allocation.getTripallocationstatus().getName()
        );

        // Get target status
        Tripallocationstatus rejectedStatus = tripAllocationStatusRepository.findByName("Rejected")
                .orElseThrow(() -> new ResourceNotFoundException("Status 'Rejected' not found"));

        // Validate transition using State Pattern
        currentState.transitionTo(allocation, rejectedStatus);

        // Save
        Tripcrewallocation saved = tripCrewAllocationRepository.save(allocation);

        log.info("Allocation rejected: {} - {} for trip {}",
                saved.getEmployee().getNumber(), saved.getRole().getName(), saved.getTrip().getId());

        return tripCrewAllocationMapper.toDto(saved);
    }

    /**
     * Clear all rejected allocations for a trip.
     */
    @Transactional
    public void clearRejectedAllocations(Integer tripId) {
        log.info("Clearing rejected allocations for trip: {}", tripId);

        List<Tripcrewallocation> rejectedAllocations = tripCrewAllocationRepository
                .findByTrip_DoserviceAndTripallocationstatus_NameIn(
                        tripRepository.findById(tripId)
                                .orElseThrow(() -> new ResourceNotFoundException("Trip not found: " + tripId))
                                .getDoservice(),
                        List.of("Rejected")
                );

        tripCrewAllocationRepository.deleteAll(rejectedAllocations);

        log.info("Cleared {} rejected allocations", rejectedAllocations.size());
    }

}
