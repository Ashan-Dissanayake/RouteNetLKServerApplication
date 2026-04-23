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
public class TripService {

    private final TripRepository tripRepository;
    private final TripStatusService tripStatusService;
    private final TripMapper tripMapper;

    private final TripContextBuilder contextBuilder;
    private final List<TripValidationStrategy> strategies;
    private final TripActivationStrategy activationStrategy;
    private final TripSuspendedStrategy suspendStrategy;
    private final TripDiscontinuedStrategy discontinuedStrategy;

    @Transactional(readOnly = true)
    public List<TripDetailResponseDto> getTrips() {
        return tripMapper.toDetailList(tripRepository.findAll());
    }

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

    @Transactional(readOnly = true)
    public Trip getTripById(Integer tripId){
     return tripRepository.findById(tripId)
             .orElseThrow(()->new ResourceNotFoundException("Trip not Found"));
    }

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

    @Transactional
    public TripDetailResponseDto activateTrip(Integer tripId){
        Trip trip = getTripById(tripId);
        activationStrategy.activateTrip(trip);
        Trip activatedTrip = tripRepository.save(trip);
        return tripMapper.toDto(activatedTrip);
    }

     @Transactional
    public TripDetailResponseDto suspendTrip(Integer tripId){
         Trip trip = getTripById(tripId);
        suspendStrategy.suspendTrip(trip);
        Trip suspendedTrip = tripRepository.save(trip);
        return tripMapper.toDto(suspendedTrip);
    }

    @Transactional
    public TripDetailResponseDto discontinueTrip(Integer tripId){
        Trip trip = getTripById(tripId);
        discontinuedStrategy.discontinueTrip(trip);
        Trip discontinuedTrip = tripRepository.save(trip);
        return tripMapper.toDto(discontinuedTrip);
    }




}
