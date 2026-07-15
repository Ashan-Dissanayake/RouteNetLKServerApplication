package lk.ashan.routenetlkserverapllication.module.trip.service;


import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripStatusMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripStatusService {

    private final TripStatusRepository tripStatusRepository;
    private final TripStatusMapper tripStatusMapper;

    @Transactional(readOnly = true)
    public List<TripStatusDto> getTripStatuses() {
        return tripStatusMapper.toDtoList(tripStatusRepository.findAll());
    }


    @Transactional(readOnly = true)
    public Tripstatus getByName(String name) {
        return tripStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trip status '" + name + "' not found"
                ));
    }
}
