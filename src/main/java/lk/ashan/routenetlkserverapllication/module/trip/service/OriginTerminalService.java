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

@Service
@RequiredArgsConstructor
public class OriginTerminalService {

    private final OriginTerminalRepository originTerminalRepository;
    private final OriginTerminalMapper originTerminalMapper;

    public List<OriginTerminalDto> getOriginTerminals() {
        return originTerminalMapper.toDtoList(originTerminalRepository.findAll());
    }
}
