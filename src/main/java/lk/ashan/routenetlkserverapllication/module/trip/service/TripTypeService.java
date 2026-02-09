package lk.ashan.routenetlkserverapllication.module.trip.service;


import lk.ashan.routenetlkserverapllication.module.trip.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripTypeMapper;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripTypeService {

    private final TripTypeRepository triptypeRepository;
    private final TripTypeMapper triptypeMapper;

    public List<TripTypeDto> getTripTypes() {
        return triptypeMapper.toDtoList(triptypeRepository.findAll());
    }
}
