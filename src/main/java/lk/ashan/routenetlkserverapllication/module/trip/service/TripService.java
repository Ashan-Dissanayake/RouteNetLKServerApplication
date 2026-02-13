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
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.module.trip.planner.TripOverrideSolverService;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripState;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStatusFactory;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.TripValidationContext;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.TripValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
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
    private final TripVehicleOverrideRepository tripVehicleOverrideRepository;
    private final PermitRepository permitRepository;

    private final TripMapper tripMapper;
    private final OriginTerminalMapper originTerminalMapper;

    private final TripOverrideSolverService tripOverrideSolverService;

    private final List<TripValidationStrategy> validationStrategies;
    private final TripStatusFactory tripStatusFactory;


    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> getTrips(){
        return tripMapper.toDetailList(tripRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> searchTrips(@NotNull HashMap<String, String> params) {

        List<Trip> trips = tripRepository.findAll();

        if (!params.isEmpty()) {

            String tripTypeId = params.get("sstriptype");
            String toDeparture= params.get("sstodepature");
            String tripStatusId= params.get("sstripstatus");

            Stream<Trip> tripStream = trips.stream();

            if(tripTypeId!=null)tripStream = tripStream.filter(t->t.getTriptype().getId() == Integer.parseInt(tripTypeId));
            if(toDeparture!=null)tripStream = tripStream.filter(t-> t.getTodepature() == LocalTime.parse(toDeparture));
            if(tripStatusId!=null)tripStream = tripStream.filter(t->t.getTripstatus().getId() == Integer.parseInt(tripStatusId));

            return tripMapper.toDetailList( tripStream.collect(Collectors.toList()));

        }

        return tripMapper.toDetailList(trips);

    }

    @Transactional
    public TripDetailResponseDto createTrip(@NotNull TripCreateRequestDto createRequestDto){

        List<Trip> permitRouteOriginExTrips = tripRepository
                .findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                        createRequestDto.getPermite().getRoute().getId(),
                        createRequestDto.getOriginterminal().getId(),
                        createRequestDto.getDoservice()
                );

        Route route = routeRepository.findById(createRequestDto.getPermite().getRoute().getId()).orElseThrow(() ->
                new ResourceNotFoundException("Route not found with given permit")
        );

        List<Trip> permitDoServiceExTrips = tripRepository.findByPermite_IdAndDoservice(
                createRequestDto.getPermite().getId(),
                createRequestDto.getDoservice()
        );

        TripValidationContext context = TripValidationContext.builder()
                .permitRouteOriginExTrips(permitRouteOriginExTrips)
                .minGapMinutes(route.getMingapminutes())
                .requestedDeparture(createRequestDto.getTodepature())
                .tripNo(createRequestDto.getNotrip())
                .permitDoServiceExTrips(permitDoServiceExTrips)
                .build();

        validationStrategies.forEach(strategy -> strategy.validate(context));

        Trip trip = tripMapper.toEntity(createRequestDto);

        Tripstatus reqestTripStatus = tripStatusRepository.findByName(createRequestDto.getTripstatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trp status not found: " + createRequestDto.getTripstatus().getName()));

        TripState state = tripStatusFactory.getState(reqestTripStatus.getName());
        state.validateInitial();

        //due to validate initial calls empty body after processing need explicit set
        trip.setTripstatus(reqestTripStatus);

        Trip savedTrip = tripRepository.save(trip);

        return tripMapper.toDto(savedTrip);
    }

    @Transactional
    public OverrideSuggestionResponse triggerOverrideSolver(Integer tripId) {

        // Load trip
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        //Validate status
        if (trip.getTripstatus().getName().equalsIgnoreCase("NEEDS VEHICLE OVERRIDE")) {
            throw new IllegalStateException(
                    "Override solver can only run for NEEDS_VEHICLE_OVERRIDE trips"
            );
        }

        //Ensure trip not cancelled/completed
        if (trip.getTripstatus().getName().equalsIgnoreCase("CANCELLED") ||
                trip.getTripstatus().getName().equalsIgnoreCase("COMPLETED")) {
            throw new IllegalStateException("Trip already closed");
        }

        //Load candidate vehicles (same depot, not soft deleted)
        List<Vehicle> candidateVehicles =
                vehicleRepository.findByBranch_IdAndDeletedFalse(
                        trip.getBranch().getId()
                );

        if (candidateVehicles.isEmpty()) {
            return new OverrideSuggestionResponse(tripId, null);
        }

        //Load existing trips on same service date
        List<Trip> existingTrips =
                tripRepository.findByDoserviceAndTripstatus_NameIn(
                        trip.getDoservice(),
                        List.of(
                                "Ready",
                               "In progress",
                                "Delayed",
                                "Suspended"
                        )
                );

        //Remove current trip from conflict list
        existingTrips.removeIf(t -> t.getId().equals(trip.getId()));

        //Call solver
        Vehicle suggestedVehicle =
                tripOverrideSolverService.solveForTrip(trip, candidateVehicles, existingTrips);

        //Return suggestion (do NOT persist yet)
        return new OverrideSuggestionResponse(
                trip.getId(),
                suggestedVehicle != null ? suggestedVehicle.getId() : null
        );
    }

    @Transactional
    public TripDetailResponseDto approveOverride(Integer tripId,Integer vehicleId){

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Trip not found"));

        if (!trip.getTripstatus().getName().equalsIgnoreCase("NEEDS VEHICLE OVERRIDE")) {
            throw new IllegalStateException(
                    "Override approval allowed only for NEEDS_VEHICLE_OVERRIDE trips"
            );
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        if (!vehicle.getBranch().getId().equals(trip.getBranch().getId())) {
            throw new IllegalStateException("Vehicle must belong to same depot");
        }

        if (!vehicle.getVehiclestatus().getName().equalsIgnoreCase("AVAILABLE")) {
            throw new IllegalStateException("Vehicle not available");
        }

        boolean conflictExists =
                tripRepository.existsVehicleConflictForOverride(
                        vehicle.getId(),
                        trip.getTodepature(),
                        trip.getToarrival(),
                        trip.getId()
                );

        if (conflictExists) {
            throw new IllegalStateException(
                    "Vehicle already assigned to another overlapping trip"
            );
        }

        Tripvehicleoverride override = new Tripvehicleoverride();
        override.setTrip(trip);
        override.setVehicle(vehicle);
        tripVehicleOverrideRepository.save(override);

        Tripstatus readyStatus = tripStatusRepository.findByName("Ready")
                .orElseThrow(()-> new ResourceNotFoundException("Status not found"));

        trip.setTripstatus(readyStatus);
        tripRepository.save(trip);

        return tripMapper.toDto(trip);
    }

    @Transactional
    public TripDetailResponseDto  updateTrip(@NotNull TripUpdateRequestDto requestDto){

        Trip trip = tripRepository.findById(requestDto.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Trip not found"));


        Tripstatus tripstatus = trip.getTripstatus();

        if (tripstatus.getName().equalsIgnoreCase("CANCELLED") || tripstatus.getName().equalsIgnoreCase("COMPLETED")){
            throw new IllegalArgumentException("Closed trips can not be edit");
        }

        if (tripstatus.getName().equalsIgnoreCase("READY")) {

            // Permit cannot change
            if (!trip.getPermite().getId().equals(requestDto.getPermite().getId())) {
                throw new IllegalStateException("Permit cannot be changed once READY");
            }
        }

        validateMinGap(
                trip.getPermite().getRoute().getId(),
                requestDto.getOriginterminal().getId(),
                requestDto.getDoservice(),
                requestDto.getTodepature(),
                trip.getId()
        );

        trip.setTodepature(requestDto.getTodepature());
        trip.setDoservice(requestDto.getDoservice());
        trip.setOriginterminal(originTerminalMapper.toEntity(requestDto.getOriginterminal()));

         /*
        Permite reqestPermite =  permitRepository.findById(requestDto.getPermite().getId())
                .orElseThrow(()-> new ResourceNotFoundException("Permit not found"));


        Vehicle permitVehicle = vehicleRepository.findByNumber(reqestPermite.getVehicle().getNumber())
                .orElseThrow(()-> new ResourceNotFoundException("Vehicle not found"));


        if (!permitVehicle.getVehiclestatus().getName().equalsIgnoreCase("AVAILABLE")) {


            Tripstatus needVehicleOverrideStatus = tripStatusRepository.findByName("Need vehicle override")
                    .orElseThrow(()-> new ResourceNotFoundException("Status not found"));

            trip.setTripstatus(needVehicleOverrideStatus);

            Vehicle suggested =
                    tripOverrideSolverService.solveForTrip(
                            trip,
                            vehicleRepository.findByBranch_IdAndDeletedFalse(
                                    trip.getBranch().getId()
                            ),
                                    tripRepository.findByDoserviceAndTripstatus_Name(
                                    trip.getDoservice(),
                                    "Planned"
                            )
                    );

            return tripMapper.toDto(trip);
        }
*/

        Trip updatedTrip = tripRepository.save(trip);
        return  tripMapper.toDto(updatedTrip);
    }


    private void validateMinGap(
            Integer routeId,
            Integer terminalId,
            LocalDate serviceDate,
            LocalTime departureTime,
            Integer currentTripId
    ) {

        List<Trip> trips =
                tripRepository.findByPermite_Route_IdAndOriginterminal_IdAndDoservice(
                        routeId,
                        terminalId,
                        serviceDate
                );

        int minGap = tripRepository.findRouteMinGap(routeId);

        for (Trip existing : trips) {

            if (existing.getId().equals(currentTripId)) continue;

            long diff = Math.abs(
                    Duration.between(
                            existing.getTodepature(),
                            departureTime
                    ).toMinutes()
            );

            if (diff < minGap) {
                throw new IllegalStateException(
                        "Minimum gap violation between trips"
                );
            }
        }
    }

}
