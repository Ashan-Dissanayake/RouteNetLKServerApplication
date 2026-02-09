package lk.ashan.routenetlkserverapllication.module.trip.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

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
            if(toDeparture!=null)tripStream = tripStream.filter(t-> Objects.equals(t.getTodepature(), Time.valueOf(toDeparture)));
            if(tripStatusId!=null)tripStream = tripStream.filter(t->t.getTripstatus().getId() == Integer.parseInt(tripStatusId));

            return tripMapper.toDetailList( tripStream.collect(Collectors.toList()));

        }

        return tripMapper.toDetailList(trips);

    }

}
