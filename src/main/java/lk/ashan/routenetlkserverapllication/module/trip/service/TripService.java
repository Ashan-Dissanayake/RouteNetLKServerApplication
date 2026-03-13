package lk.ashan.routenetlkserverapllication.module.trip.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OverrideSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.OriginTerminalMapper;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.planner.TripOverrideSolverService;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripState;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStatusFactory;
import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripCreateContext;
import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripCreateContextBuilder;
import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripUpdateContext;
import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripUpdateContextBuilder;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final VehicleRepository vehicleRepository;
    private final TripStatusRepository tripStatusRepository;

    private final TripMapper tripMapper;
    private final OriginTerminalMapper originTerminalMapper;

    private final TripOverrideSolverService tripOverrideSolverService;

    private final List<TripCreationValidationStrategy> creationValidationStrategies;
    private final List<TripUpdateValidationStrategy> updateValidationStrategies;
    private final TripStatusFactory tripStatusFactory;

    private final InitialTripStatusDeterminationStrategy initialStatusStrategy;
    private final TripExecutionStrategy tripExecutionStrategy;
    private final TripCancellationStrategy tripCancellationStrategy;
    private final TripCompletionStrategy tripCompletionStrategy;

    private final TripStateTransitionHandler stateTransitionHandler;
    private final VehicleOverrideApprovalStrategy overrideApprovalStrategy;

    private final TripUpdateVehicleAvailabilityValidation vehicleAvailabilityValidation;

    private final TripCreateContextBuilder validationContextBuilder;
    private final TripUpdateContextBuilder updateContextBuilder;

    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> getTrips() {
        return tripMapper.toDetailList(tripRepository.findAll());
    }

    /**
     * PRESERVED: Original search implementation with HashMap
     * ISSUE #15: Keeping in-memory filtering as per requirements
     */
    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> searchTrips(@NotNull HashMap<String, String> params) {

        List<Trip> trips = tripRepository.findAll();

        if (!params.isEmpty()) {

            String tripTypeId = params.get("sstriptype");
            String toDeparture = params.get("sstodepature");
            String tripStatusId = params.get("sstripstatus");

            Stream<Trip> tripStream = trips.stream();

            if (tripTypeId != null)
                tripStream = tripStream.filter(t -> t.getTriptype().getId() == Integer.parseInt(tripTypeId));
            if (toDeparture != null)
                tripStream = tripStream.filter(t -> t.getTodepature() == LocalTime.parse(toDeparture));
            if (tripStatusId != null)
                tripStream = tripStream.filter(t -> t.getTripstatus().getId() == Integer.parseInt(tripStatusId));

            return tripMapper.toDetailList(tripStream.collect(Collectors.toList()));
        }

        return tripMapper.toDetailList(trips);
    }

    /**
     * ISSUE #19: Now uses TripValidationContextBuilder
     * Encapsulates all query logic and context building for creation
     */
    @Transactional
    public TripDetailResponseDto createTrip(@NotNull TripCreateRequestDto createRequestDto) {

        TripCreateContext context = validationContextBuilder.buildForCreation(createRequestDto );
        creationValidationStrategies.forEach(strategy -> strategy.validate(context));

        Trip trip = tripMapper.toEntity(createRequestDto);

        Integer nextTripNo = getNextTripNumber(
                createRequestDto.getPermite().getId(),
                createRequestDto.getDoservice()
        );

        trip.setNotrip(nextTripNo);


        // Determine initial status
        Tripstatus determinedStatus = initialStatusStrategy.determineInitialStatus(
                context.getPermit(),
                createRequestDto.getDoservice(),
                createRequestDto.getTodepature(),
                createRequestDto.getToarrival()
        );


        TripState state = tripStatusFactory.getState(determinedStatus.getName());
        state.validateInitial();

        trip.setTripstatus(determinedStatus);

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toDto(savedTrip);
    }

    @Transactional
    public OverrideSuggestionResponse triggerOverrideSolver(Integer tripId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        String normalizedStatus = trip.getTripstatus().getName()
                .trim().toUpperCase().replaceAll("[\\s_-]+", "");

        if (!normalizedStatus.equals("NEEDVEHICLEOVERRIDE")) {
            throw new IllegalStateException(
                    "Override solver can only run for NEEDS_VEHICLE_OVERRIDE trips"
            );
        }

        // Load candidate vehicles
        List<Vehicle> candidateVehicles = vehicleRepository
                .findByBranch_IdAndDeletedFalse(trip.getBranch().getId());

        if (candidateVehicles.isEmpty()) {
            System.out.println("❌ NO CANDIDATES FROM REPOSITORY!");
            return new OverrideSuggestionResponse(tripId, null);
        }

        // Load existing trips
        List<Trip> existingTrips = tripRepository
                .findByDoserviceAndTripstatus_NameIn(
                        trip.getDoservice(),
                        List.of("Ready", "In progress", "Delayed", "Suspended")
                );

        existingTrips.removeIf(t -> t.getId().equals(trip.getId()));

        // Call solver
        Vehicle suggestedVehicle = tripOverrideSolverService.solveForTrip(
                trip, candidateVehicles, existingTrips
        );

        return new OverrideSuggestionResponse(
                trip.getId(),
                suggestedVehicle != null ? suggestedVehicle.getId() : null
        );
    }

    @Transactional
    public TripDetailResponseDto approveOverride(Integer tripId, Integer vehicleId) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        overrideApprovalStrategy.approveOverride(trip, vehicleId);

        Trip updatedTrip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found after approval"));

        return tripMapper.toDto(updatedTrip);
    }

    /**
     * ISSUE #19: Now uses TripUpdateContextBuilder
     * Encapsulates all query logic and context building for updates
     */
    @Transactional
    public TripDetailResponseDto updateTrip(@NotNull TripUpdateRequestDto requestDto) {

        Trip trip = tripRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        Tripstatus currentStatus = trip.getTripstatus();

        // Validate trip is not in terminal state
        if (currentStatus.getName().equalsIgnoreCase("CANCELLED") ||
                currentStatus.getName().equalsIgnoreCase("COMPLETED")) {
            throw new BusinessRuleViolationException("Closed trips cannot be edited");
        }

        // ISSUE #19: Use context builder - encapsulates all query logic and change detection
        TripUpdateContext updateContext = updateContextBuilder.buildForUpdate(
                trip,
                requestDto.getPermite().getId(),
                requestDto.getDoservice(),
                requestDto.getTodepature(),
                requestDto.getToarrival(),
                requestDto.getOriginterminal().getId()
        );

        // Run all update validation strategies
        updateValidationStrategies.forEach(strategy -> strategy.validate(updateContext));

        // Apply updates to trip entity
        trip.setTodepature(requestDto.getTodepature());
        trip.setToarrival(requestDto.getToarrival());
        trip.setDoservice(requestDto.getDoservice());
        trip.setOriginterminal(originTerminalMapper.toEntity(requestDto.getOriginterminal()));

        if (updateContext.isPermitChanged()) {
            trip.setPermite(updateContext.getNewPermit());
        }

        // Check if vehicle override is now required due to update
        if (vehicleAvailabilityValidation.requiresVehicleOverride(updateContext)) {
            Tripstatus needsOverrideStatus = tripStatusRepository.findByName("Need vehicle override'")
                    .orElseThrow(() -> new ResourceNotFoundException("NEEDS VEHICLE OVERRIDE status not found"));

            stateTransitionHandler.transitionTo(trip, needsOverrideStatus);
        }

        Trip updatedTrip = tripRepository.save(trip);
        return tripMapper.toDto(updatedTrip);
    }

    @Transactional
    public TripDetailResponseDto executeTrip(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripExecutionStrategy.executeTrip(trip);

        Trip executedTrip = tripRepository.save(trip);
        return tripMapper.toDto(executedTrip);
    }

    @Transactional
    public TripDetailResponseDto cancelTrip(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripCancellationStrategy.cancelTrip(trip);

        Tripstatus cancelledStatus = tripStatusRepository.findByName("Cancelled")
                .orElseThrow(() -> new ResourceNotFoundException("CANCELLED status not found"));

        stateTransitionHandler.transitionTo(trip, cancelledStatus);

        Trip cancelledTrip = tripRepository.save(trip);
        return tripMapper.toDto(cancelledTrip);
    }

    @Transactional
    public TripDetailResponseDto completeTrip(Integer tripId) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripCompletionStrategy.completeTrip(trip);

        Trip completedTrip = tripRepository.save(trip);
        return tripMapper.toDto(completedTrip);
    }

    @Transactional
    public TripDetailResponseDto completeTrip(Integer tripId, LocalTime actualArrivalTime) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripCompletionStrategy.completeTrip(trip, actualArrivalTime);

        Trip completedTrip = tripRepository.save(trip);
        return tripMapper.toDto(completedTrip);
    }

    private Integer getNextTripNumber(Integer permitId, LocalDate serviceDate) {
        return tripRepository.findMaxTripNumberForPermitAndDate(permitId, serviceDate)
                .map(max -> max + 1)
                .orElse(1);  // First trip = 1
    }
}
