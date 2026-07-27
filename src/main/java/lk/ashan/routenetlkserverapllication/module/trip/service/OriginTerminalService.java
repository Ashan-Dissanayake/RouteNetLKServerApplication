package lk.ashan.routenetlkserverapllication.module.trip.service;


import lk.ashan.routenetlkserverapllication.module.trip.mapper.OriginTerminalMapper;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripTypeMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OriginTerminalRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing Origin Terminals.
 * Provides methods to retrieve origin terminal data.
 */
@Service
@RequiredArgsConstructor
public class OriginTerminalService {

    private final OriginTerminalRepository originTerminalRepository;
    private final OriginTerminalMapper originTerminalMapper;

    /**
     * Retrieves a list of all origin terminals.
     *
     * @return a list of {@link OriginTerminalDto} objects representing the origin terminals.
     */
    public List<OriginTerminalDto> getOriginTerminals() {
        return originTerminalMapper.toDtoList(originTerminalRepository.findAll());
    }
}
