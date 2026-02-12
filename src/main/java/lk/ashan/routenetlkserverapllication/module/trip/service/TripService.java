package lk.ashan.routenetlkserverapllication.module.trip.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.permit.model.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripState;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStatusFactory;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.TripValidationContext;
import lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy.TripValidationStrategy;
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
    private final TripStatusRepository tripStatusRepository;
    private final TripMapper tripMapper;

    private final List<TripValidationStrategy> validationStrategies;
    private final TripStatusFactory tripStatusFactory;

    public List<TripDetailResponseDto> getTrips(){
        return tripMapper.toDetailList(tripRepository.findAll());
    }

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

}
