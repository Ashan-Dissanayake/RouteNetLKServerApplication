package lk.ashan.routenetlkserverapllication.module.trip.service;


import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripTypeMapper;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Trip Types.
 * Provides methods to retrieve trip type data.
 */
@Service
@RequiredArgsConstructor
public class TripTypeService {

    private final TripTypeRepository triptypeRepository;
    private final TripTypeMapper triptypeMapper;

    /**
     * Retrieves all trip types.
     *
     * @return a list of TripTypeDto objects representing all trip types.
     */
    @Transactional(readOnly = true)
    public List<TripTypeDto> getTripTypes() {
        return triptypeMapper.toDtoList(triptypeRepository.findAll());
    }
}
