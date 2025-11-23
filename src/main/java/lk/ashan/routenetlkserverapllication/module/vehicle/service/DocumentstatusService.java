package lk.ashan.routenetlkserverapllication.module.vehicle.service;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.DocumentstatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.DocumentstatusMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.DocumentstatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentstatusService {

    private final DocumentstatusRepository documentstatusRepository;
    private final DocumentstatusMapper vehiclestatusMapper;

    public List<DocumentstatusDto> getDocumentStatus(){
       return vehiclestatusMapper.toDtoList(documentstatusRepository.findAll());
    }

}
