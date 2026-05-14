package lk.ashan.routenetlkserverapllication.module.incident.service;


import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentTypeMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentTypeDto;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentTypeService {
    
    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentTypeMapper incidentTypeMapper;

    @Transactional(readOnly = true)
    public List<IncidentTypeDto> getIncidentTypes(){
       return incidentTypeMapper.toDtoList(incidentTypeRepository.findAll());
    }
    
}
