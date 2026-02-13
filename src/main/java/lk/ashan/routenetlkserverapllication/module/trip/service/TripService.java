package lk.ashan.routenetlkserverapllication.module.trip.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.dto.OverrideSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.OriginTerminalMapper;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.planner.TripOverrideSolverService;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripState;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStatusFactory;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;
    private final TripStatusRepository tripStatusRepository;
    private final PermitRepository permitRepository;

    private final TripMapper tripMapper;
    private final OriginTerminalMapper originTerminalMapper;

    private final TripOverrideSolverService tripOverrideSolverService;

    private final List<TripValidationStrategy> validationStrategies;
    private final TripStatusFactory tripStatusFactory;

    // Priority 1 strategies
    private final InitialTripStatusDeterminationStrategy initialStatusStrategy;
    private final TripExecutionStrategy tripExecutionStrategy;
    private final TripCancellationStrategy tripCancellationStrategy;
    private final TripCompletionStrategy tripCompletionStrategy;

    // Priority 2 components
    private final TripStateTransitionHandler stateTransitionHandler;
    private final VehicleConflictDetectionStrategy conflictDetectionStrategy;
    private final VehicleOverrideApprovalStrategy overrideApprovalStrategy;

    // Priority 3 components - ISSUE #5
    private final List<TripUpdateValidationStrategy> updateValidationStrategies;
    private final TripUpdateVehicleAvailabilityValidation vehicleAvailabilityValidation;

    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> getTrips() {
        return tripMapper.toDetailList(tripRepository.findAll());
    }

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

    @Transactional
    public TripDetailResponseDto createTrip(@NotNull TripCreateRequestDto createRequestDto) {

        Permite permit = permitRepository.findById(createRequestDto.getPermite().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Permit not found"));

        List<Trip> permitRouteOriginExTrips = tripRepository
                .findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                        permit.getRoute().getId(),
                        createRequestDto.getOriginterminal().getId(),
                        createRequestDto.getDoservice()
                );

        Route route = routeRepository.findById(permit.getRoute().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with given permit"));

        List<Trip> permitDoServiceExTrips = tripRepository.findByPermite_IdAndDoservice(
                permit.getId(),
                createRequestDto.getDoservice()
        );

        TripValidationContext context = TripValidationContext.builder()
                .permitRouteOriginExTrips(permitRouteOriginExTrips)
                .minGapMinutes(route.getMingapminutes())
                .requestedDeparture(createRequestDto.getTodepature())
                .tripNo(createRequestDto.getNotrip())
                .permitDoServiceExTrips(permitDoServiceExTrips)
                .permit(permit)
                .serviceDate(createRequestDto.getDoservice())
                .build();

        validationStrategies.forEach(strategy -> strategy.validate(context));

        Trip trip = tripMapper.toEntity(createRequestDto);

        Tripstatus determinedStatus = initialStatusStrategy.determineInitialStatus(
                permit,
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

        if (!trip.getTripstatus().getName().equalsIgnoreCase("NEEDS VEHICLE OVERRIDE")) {
            throw new IllegalStateException(
                    "Override solver can only run for NEEDS_VEHICLE_OVERRIDE trips"
            );
        }

        if (trip.getTripstatus().getName().equalsIgnoreCase("CANCELLED") ||
                trip.getTripstatus().getName().equalsIgnoreCase("COMPLETED")) {
            throw new IllegalStateException("Trip already closed");
        }

        List<Vehicle> candidateVehicles =
                vehicleRepository.findByBranch_IdAndDeletedFalse(
                        trip.getBranch().getId()
                );

        if (candidateVehicles.isEmpty()) {
            return new OverrideSuggestionResponse(tripId, null);
        }

        List<Trip> existingTrips =
                tripRepository.findByDoserviceAndTripstatus_NameIn(
                        trip.getDoservice(),
                        List.of("READY", "IN_PROGRESS", "DELAYED", "SUSPENDED")
                );

        existingTrips.removeIf(t -> t.getId().equals(trip.getId()));

        Vehicle suggestedVehicle =
                tripOverrideSolverService.solveForTrip(trip, candidateVehicles, existingTrips);

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
     * ISSUE #5: Completely refactored update logic with consistent validation
     * ISSUE #13: Removed commented code, implemented proper vehicle availability checking
     */
    @Transactional
    public TripDetailResponseDto updateTrip(@NotNull TripUpdateRequestDto requestDto) {

        Trip trip = tripRepository.findById(requestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        Tripstatus currentStatus = trip.getTripstatus();

        // Validate trip is not in terminal state
        if (currentStatus.getName().equalsIgnoreCase("CANCELLED") ||
                currentStatus.getName().equalsIgnoreCase("COMPLETED")) {
            throw new IllegalArgumentException("Closed trips cannot be edited");
        }

        // Determine what changed
        boolean permitChanged = !trip.getPermite().getId().equals(requestDto.getPermite().getId());
        boolean timeChanged = !trip.getTodepature().equals(requestDto.getTodepature()) ||
                !trip.getToarrival().equals(requestDto.getToarrival());
        boolean dateChanged = !trip.getDoservice().equals(requestDto.getDoservice());
        boolean terminalChanged = !trip.getOriginterminal().getId().equals(requestDto.getOriginterminal().getId());

        // Load new permit if changed
        Permite newPermit = null;
        if (permitChanged) {
            newPermit = permitRepository.findById(requestDto.getPermite().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("New permit not found"));
        }

        // Get route for validation (from new permit if changed, else existing)
        Route route = permitChanged
                ? newPermit.getRoute()
                : trip.getPermite().getRoute();

        // Load existing trips for validation
        List<Trip> permitRouteOriginExTrips = tripRepository
                .findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                        route.getId(),
                        requestDto.getOriginterminal().getId(),
                        requestDto.getDoservice()
                );

        // Build update context
        TripUpdateContext updateContext = TripUpdateContext.builder()
                .existingTrip(trip)
                .newServiceDate(requestDto.getDoservice())
                .newDeparture(requestDto.getTodepature())
                .newArrival(requestDto.getToarrival())
                .newOriginTerminalId(requestDto.getOriginterminal().getId())
                .newPermitId(requestDto.getPermite().getId())
                .permitRouteOriginExTrips(permitRouteOriginExTrips)
                .minGapMinutes(route.getMingapminutes())
                .newPermit(newPermit)
                .permitChanged(permitChanged)
                .timeChanged(timeChanged)
                .dateChanged(dateChanged)
                .terminalChanged(terminalChanged)
                .build();

        // Run all update validation strategies
        updateValidationStrategies.forEach(strategy -> strategy.validate(updateContext));

        // Apply updates to trip entity
        trip.setTodepature(requestDto.getTodepature());
        trip.setToarrival(requestDto.getToarrival());
        trip.setDoservice(requestDto.getDoservice());
        trip.setOriginterminal(originTerminalMapper.toEntity(requestDto.getOriginterminal()));

        if (permitChanged) {
            trip.setPermite(newPermit);
        }

        // ISSUE #13: Check if vehicle override is now required due to update
        if (vehicleAvailabilityValidation.requiresVehicleOverride(updateContext)) {
            Tripstatus needsOverrideStatus = tripStatusRepository.findByName("NEEDS VEHICLE OVERRIDE")
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

        Tripstatus cancelledStatus = tripStatusRepository.findByName("CANCELLED")
                .orElseThrow(() -> new ResourceNotFoundException("CANCELLED status not found"));

        stateTransitionHandler.transitionTo(trip, cancelledStatus);

        Trip cancelledTrip = tripRepository.save(trip);
        return tripMapper.toDto(cancelledTrip);
    }

    @Transactional
    public TripDetailResponseDto cancelTrip(Integer tripId, boolean hasAuthorization) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        tripCancellationStrategy.cancelTrip(trip, hasAuthorization);

        Tripstatus cancelledStatus = tripStatusRepository.findByName("CANCELLED")
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
}
