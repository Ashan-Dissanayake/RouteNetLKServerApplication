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

/**
 * Service class for managing Trip Status operations.
 */
@Service
@RequiredArgsConstructor
public class TripStatusService {

    private final TripStatusRepository tripStatusRepository;
    private final TripStatusMapper tripStatusMapper;

    /**
     * Retrieves all trip statuses.
     *
     * @return a list of TripStatusDto objects representing all trip statuses.
     */
    @Transactional(readOnly = true)
    public List<TripStatusDto> getTripStatuses() {
        return tripStatusMapper.toDtoList(tripStatusRepository.findAll());
    }

    /**
     * Retrieves a Tripstatus entity by its name.
     *
     * @param name the name of the trip status to retrieve.
     * @return the Tripstatus entity matching the given name.
     * @throws ResourceNotFoundException if no trip status with the given name is found.
     */
    @Transactional(readOnly = true)
    public Tripstatus getByName(String name) {
        return tripStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Trip status '" + name + "' not found"
                ));
    }
}
