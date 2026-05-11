package lk.ashan.routenetlkserverapllication.module.tripexecution.service;

import ai.timefold.solver.core.api.solver.SolverJob;
import ai.timefold.solver.core.api.solver.SolverManager;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.mapper.TripExecutionMapper;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionInitializationDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.planner.*;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.state.TripExecutionTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripExecutionService {

    private final TripRepository tripRepository;
    private final TripExecutionRepository tripExecutionRepository;
    private final TripExecutionStatusService tripExecutionStatusService;
    private final VehicleRepository vehicleRepository;
    private final DriverRepository driverRepository;
    private final ConductorRepository conductorRepository;

    private final BranchService branchService;
    private final TripExecutionMapper tripExecutionMapper;

    private final TripExecutionTransitionHandler tripExecutionTransitionHandler;

    @Qualifier("tripExecutionSolver")
    private final SolverManager<TripExecutionSolution, Integer> tripExecutionSolverManager;

    @Transactional(readOnly = true)
    public List<TripExecutionDetailsResponseDto> getTripExecutions(){
        return tripExecutionMapper.toDtoList(tripExecutionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TripExecutionDetailsResponseDto> searchTripExecutions(@NotNull HashMap<String, String> params) {

        List<TripExecution> tripExecutions = tripExecutionRepository.findAll();

        if (!params.isEmpty()) {

            String doservice = params.get("ssdoservice");
            String tripexecutionstatusId= params.get("sstripexecutionstatus");

            Stream<TripExecution> tripExecutionStream = tripExecutions.stream();

            if(doservice!=null)tripExecutionStream = tripExecutionStream.filter(t->t.getDoservice().isEqual(LocalDate.parse(doservice)));
            if(tripexecutionstatusId!=null)tripExecutionStream = tripExecutionStream.filter(t->t.getTripexecutionstatus().getId()==Integer.parseInt(tripexecutionstatusId));

            return tripExecutionMapper.toDtoList( tripExecutionStream.collect(Collectors.toList()));
        }
        return tripExecutionMapper.toDtoList(tripExecutions);
    }

    @Transactional(readOnly = true)
    public List<TripExecution> getTripExecutionByTripId(@NotNull Integer tripId){
        return tripExecutionRepository.findAllByTrip_Id(tripId).
                orElseThrow(()->new ResourceNotFoundException("TripExecution with id "+tripId+" not found"));
    }

    @Transactional
    public List<TripExecutionDetailsResponseDto> initializeDailyExecutions(
            @NotNull  TripExecutionInitializationDto tripExecutionInitializationDto
    ) {

        Integer branchId = tripExecutionInitializationDto.getBranch().getId();
        LocalDate executionDate = tripExecutionInitializationDto.getDoservice();

        System.out.println(executionDate);

        // 1. Fetch the "Scheduled" status (the starting state for all new executions)
        TripExecutionStatus scheduledStatus = tripExecutionStatusService.getByName("Scheduled");
        Branch branch =  branchService.getById(branchId);

        // 2. Fetch all active trips for this branch
        List<Trip> activeTrips = tripRepository.findByBranch_IdAndTripstatus_Name(branchId, "Active");

        // 3. Identify trips already initialized for this date to avoid duplicates
        Set<Integer> alreadyInitializedTripIds = tripExecutionRepository
                .findByDoserviceAndBranch_Id(executionDate, branchId)
                .stream()
                .map(exec -> exec.getTrip().getId())
                .collect(Collectors.toSet());

        List<TripExecution> newExecutions = new ArrayList<>();

        for (Trip trip : activeTrips) {
            // 4. Use your Domain Method to check if the trip should run today
            if (trip.getOpcalender().isWorkingDay(executionDate)) {

                // 5. Skip if already initialized (Idempotency check)
                if (alreadyInitializedTripIds.contains(trip.getId())) {
                    continue;
                }

                // 6. Map Trip Template to Trip Execution "Shell" using Builder
                TripExecution execution = TripExecution.builder()
                        .trip(trip)
                        .branch(branch)
                        .doservice(executionDate)
                        .tripexecutionstatus(scheduledStatus)
                        // Note: vehicle, driver, conductor, and actual times are omitted,
                        // they will default to null in the "Scheduled" state.
                        .build();

                newExecutions.add(execution);
            }
        }

        // 7. Batch save for performance
        if (!newExecutions.isEmpty()) {
           List<TripExecution> savedTripExecutions = tripExecutionRepository.saveAll(newExecutions);
           return tripExecutionMapper.toDtoList(savedTripExecutions);
        }

        List<TripExecution> allTodayExecutions = tripExecutionRepository.
                findByDoserviceAndBranch_Id(executionDate, branchId);
        return tripExecutionMapper.toDtoList(allTodayExecutions);

    }

    @Transactional
    public void generateTripExecutionAssignments(@NotNull TripExecutionAssignmentDto tripExecutionAssignmentDto) {

        Integer branchId = tripExecutionAssignmentDto.getBranchId();
        LocalDate executionDate = tripExecutionAssignmentDto.getDate();

        // 1. Fetch unassigned TripExecutions for the specific date and branch
        List<TripExecution> entities = tripExecutionRepository
                .findByDoserviceAndBranch_IdAndDriverIsNull(executionDate, branchId);

        if (entities.isEmpty()) {
            log.warn("No unassigned trip executions found for Date: {} and Branch: {}", executionDate, branchId);
            return;
        }

        // 2. Fetch Resources (Vehicles, Drivers, Conductors) and map to Facts
        List<VehicleFact> vehicleFacts = vehicleRepository.findByBranch_Id(branchId).stream()
                .map(tripExecutionMapper::toVehicleFact)
                .toList();

        List<CrewFact> driverFacts = driverRepository.findByEmployee_Branch_Id(branchId).stream()
                .map(tripExecutionMapper::toCrewFact)
                .toList();

        List<CrewFact> conductorFacts = conductorRepository.findByEmployee_Branch_Id(branchId).stream()
                .map(tripExecutionMapper::toCrewFact)
                .toList();

        // 3. Prepare Planning Entities from JPA Entities
        List<TripExecutionPlanning> planningEntities = entities.stream()
                .map(entity -> {
                    TripExecutionPlanning planning = tripExecutionMapper.toPlanning(entity);

                    // Logic: Map DB values to Fact logic used in ConstraintProvider
                    planning.setDepartureTime(entity.getTrip().getTodepature()); // LocalTime
                    planning.setArrivalTime(entity.getTrip().getToarrival());     // LocalTime

                    // Initialize RouteFact for familiarity checks
                    RouteFact routeFact = new RouteFact(
                            entity.getTrip().getPermite().getRoute().getId(),
                            entity.getTrip().getPermite().getRoute().getRequiredroutefamiliaritylevel().getId(),
                            entity.getTrip().getPermite().getRoute().getDistancekm()
                    );
                    planning.setRoute(routeFact);

                    return planning;
                })
                .toList();

        // 4. Build the Solution object (The Problem Fact)
        // Using an ID combined from branch and date as the problem ID
        Integer problemId = (executionDate.toString() + branchId).hashCode();
        TripExecutionSolution problem = new TripExecutionSolution(
                problemId,
                vehicleFacts,
                driverFacts,
                conductorFacts,
                planningEntities
        );

        log.info("Starting Synchronous Solver for Trip Executions on {} at Branch {}", executionDate, branchId);

        try {
            // 5. Trigger the Solver and WAIT for completion
            SolverJob<TripExecutionSolution, Integer> solverJob =
                    tripExecutionSolverManager.solve(problemId, problem);
            TripExecutionSolution finalSolution = solverJob.getFinalBestSolution();

            // 6. Map results back to JPA Entities and save
            this.saveResults(finalSolution);

        } catch (InterruptedException | ExecutionException e) {
            log.error("Solver failed for Trip Execution assignments on {}", executionDate, e);
            throw new RuntimeException("Trip assignment failed during optimization", e);
        }
    }

    private void saveResults(TripExecutionSolution finalSolution) {
        for (TripExecutionPlanning planning : finalSolution.getTripExecutionList()) {
            TripExecution entity = tripExecutionRepository.findById(planning.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("TripExecution not found for ID: " + planning.getId()));

            // Update with optimal assignments using reference proxies for performance
            if (planning.getVehicle() != null) {
                entity.setVehicle(vehicleRepository.getReferenceById(planning.getVehicle().getId()));
            }
            if (planning.getDriver() != null) {
                entity.setDriver(driverRepository.getReferenceById(planning.getDriver().getId()));
            }
            if (planning.getConductor() != null) {
                entity.setConductor(conductorRepository.getReferenceById(planning.getConductor().getId()));
            }

            tripExecutionRepository.save(entity);
        }
        log.info("Successfully saved solver assignments for {} trips.", finalSolution.getTripExecutionList().size());
    }

    @Transactional
    public void checkedInTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));

        Employee d = execution.getDriver().getEmployee();
        Employee c = execution.getConductor().getEmployee();

        if (d.getEmployeestatus().getName().equals("On leave"))
            throw new BusinessRuleViolationException("Driver is not Available on Today");

        if (c.getEmployeestatus().getName().equals("On leave"))
            throw new BusinessRuleViolationException("Conductor is not Available on Today");

        TripExecutionStatus checkedIndStatus = tripExecutionStatusService.getByName("Checked In");
        tripExecutionTransitionHandler.transitionTo(execution, checkedIndStatus);
    }

    @Transactional
    public void dispatchedTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));

        execution.setToactualdeparture(LocalTime.now());

        Vehicle vehicle = vehicleRepository.findById(execution.getVehicle().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle Not Found"));

        Integer lastMileage = vehicle.getMileage();
        execution.setStartodometer(lastMileage);

        TripExecutionStatus dispatchedStatus = tripExecutionStatusService.getByName("Dispatched");
        tripExecutionTransitionHandler.transitionTo(execution, dispatchedStatus);
    }

    @Transactional
    public void  arrivedTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));

        Integer routeDistance = execution.getTrip().getPermite().getRoute().getDistancekm();
        Integer startOdo = execution.getStartodometer();

        Integer calculatedEndOdo = startOdo + routeDistance;
        execution.setEndodometer(calculatedEndOdo);
        execution.setToactualarrival(LocalTime.now());

        //this should handle via event in later
        Vehicle vehicle = execution.getVehicle();
        vehicle.setMileage(calculatedEndOdo);
        vehicleRepository.save(vehicle);

        TripExecutionStatus arrivedStatus = tripExecutionStatusService.getByName("Arrived");
        tripExecutionTransitionHandler.transitionTo(execution, arrivedStatus);
    }

    @Transactional
    public void  breakdownTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));

        /*
        1. The Logical Flow
        Stop the Clock: Record the time the breakdown was reported.

        Lock the Odometer: Since the bus can no longer move, the current mileage
         is recorded (if known) or estimated.
        Disable the Vehicle: Update the Vehicle table status to "Under Repair" so the
        Solver/Scheduler doesn't assign it to a new trip.

        Manage the Crew: In some systems, the crew stays with the broken bus;
        in others, they are "Released" to take a relief bus.
        *
         */

        TripExecutionStatus arrivedStatus = tripExecutionStatusService.getByName("Breakdown");
        tripExecutionTransitionHandler.transitionTo(execution, arrivedStatus);
    }

    @Transactional
    public void  completedTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));

        TripExecutionStatus completedStatus = tripExecutionStatusService.getByName("Completed");
        tripExecutionTransitionHandler.transitionTo(execution, completedStatus);
    }

    @Transactional
    public void  cancelledTripExecution(@NotNull Integer tripExecutionId) {
        TripExecution execution = tripExecutionRepository.findById(tripExecutionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "TripExecution not found with id " + tripExecutionId
                ));
        /*
        The Logical Flow
        Release the Vehicle: Change the Vehicle status from "Assigned" or "Reserved" back to
        "Available".

        Release the Crew: Update the Employee status for both the Driver and Conductor back to
        "Available".

        Wipe Operational Data: If the trip was "Checked In," there might be partial data.
        You should ensure no "Actual Departure" times are saved.

        Audit Reason: (Optional but recommended) Capture why it was cancelled
        (e.g., "Low passenger count," "Driver emergency").
       */
        TripExecutionStatus cancelledStatus = tripExecutionStatusService.getByName("Cancelled");
        tripExecutionTransitionHandler.transitionTo(execution, cancelledStatus);
    }

}
