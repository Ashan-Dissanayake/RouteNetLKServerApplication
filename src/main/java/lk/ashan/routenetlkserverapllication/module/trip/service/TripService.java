package lk.ashan.routenetlkserverapllication.module.trip.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.*;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lk.ashan.routenetlkserverapllication.shared.transaction.DisableUserFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service class for managing trips. Provides methods for retrieving, creating, and updating trip data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TripService {

    private final TripRepository tripRepository;
    private final TripStatusService tripStatusService;
    private final TripMapper tripMapper;

    private final TripContextBuilder contextBuilder;
    private final List<TripValidationStrategy> strategies;
    private final TripActivationStrategy activationStrategy;
    private final TripSuspendedStrategy suspendStrategy;
    private final TripDiscontinuedStrategy discontinuedStrategy;

    /**
     * Retrieves all trips.
     *
     * @return a list of {@link TripDetailResponseDto} containing details of all trips.
     */
    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> getTrips() {
        return tripMapper.toDetailList(tripRepository.findAll());
    }

    /**
     * Searches for trips based on the provided parameters.
     *
     * @param params a {@link HashMap} containing search parameters such as trip type and status.
     * @return a list of {@link TripDetailResponseDto} matching the search criteria.
     */
    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> searchTrips(@NotNull HashMap<String, String> params) {

        List<Trip> trips = tripRepository.findAll();

        if (!params.isEmpty()) {

            String tripTypeId = params.get("sstriptype");
            String tripStatusId = params.get("sstripstatus");

            Stream<Trip> tripStream = trips.stream();

            if (tripTypeId != null)
                tripStream = tripStream.filter(t -> t.getTriptype().getId() == Integer.parseInt(tripTypeId));
            if (tripStatusId != null)
                tripStream = tripStream.filter(t -> t.getTripstatus().getId() == Integer.parseInt(tripStatusId));

            return tripMapper.toDetailList(tripStream.collect(Collectors.toList()));
        }

        return tripMapper.toDetailList(trips);
    }

    /**
     * Retrieves a trip by its ID.
     *
     * @param tripId the ID of the trip to retrieve.
     * @return the {@link Trip} entity.
     * @throws ResourceNotFoundException if the trip is not found.
     */
    @Transactional(readOnly = true)
    public Trip getTripById(Integer tripId){
     return tripRepository.findById(tripId)
             .orElseThrow(()->new ResourceNotFoundException("Trip not Found"));
    }

    /**
     * Creates a new trip.
     *
     * @param createRequestDto the {@link TripCreateRequestDto} containing trip creation details.
     * @return the created trip as a {@link TripDetailResponseDto}.
     */
    @Transactional
    public TripDetailResponseDto createTrip(@NotNull TripCreateRequestDto createRequestDto) {

        TripValidationContext context = contextBuilder.buildForCreate(createRequestDto);
        strategies.forEach(strategy -> strategy.validateCreate(context));

        Tripstatus initialStatus = tripStatusService.getByName("Draft");

        Trip entity = tripMapper.toEntity(createRequestDto);
        entity.setTripstatus(initialStatus);

        Trip savedTrip = tripRepository.save(entity);
        return tripMapper.toDto(savedTrip);
    }

    /**
     * Activates a trip by its ID.
     *
     * @param tripId the ID of the trip to activate.
     * @return the activated trip as a {@link TripDetailResponseDto}.
     */
    @Transactional
    public TripDetailResponseDto activateTrip(Integer tripId){
        Trip trip = getTripById(tripId);
        activationStrategy.activateTrip(trip);
        Trip activatedTrip = tripRepository.save(trip);
        return tripMapper.toDto(activatedTrip);
    }

    /**
     * Suspends a trip by its ID.
     *
     * @param tripId the ID of the trip to suspend.
     * @return the suspended trip as a {@link TripDetailResponseDto}.
     */
    @Transactional
    public TripDetailResponseDto suspendTrip(Integer tripId){
         Trip trip = getTripById(tripId);
        suspendStrategy.suspendTrip(trip);
        Trip suspendedTrip = tripRepository.save(trip);
        return tripMapper.toDto(suspendedTrip);
    }

    /**
     * Discontinues a trip by its ID.
     *
     * @param tripId the ID of the trip to discontinue.
     * @return the discontinued trip as a {@link TripDetailResponseDto}.
     */
    @Transactional
    public TripDetailResponseDto discontinueTrip(Integer tripId){
        Trip trip = getTripById(tripId);
        discontinuedStrategy.discontinueTrip(trip);
        Trip discontinuedTrip = tripRepository.save(trip);
        return tripMapper.toDto(discontinuedTrip);
    }
}
