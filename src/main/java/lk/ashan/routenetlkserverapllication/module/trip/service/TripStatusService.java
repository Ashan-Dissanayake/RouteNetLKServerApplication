package lk.ashan.routenetlkserverapllication.module.trip.service;


import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripStatusMapper;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripStatusService {

    private final TripStatusRepository tripStatusRepository;
    private final TripStatusMapper tripStatusMapper;

    public List<TripStatusDto> getTripStatuses() {
        return tripStatusMapper.toDtoList(tripStatusRepository.findAll());
    }
}
