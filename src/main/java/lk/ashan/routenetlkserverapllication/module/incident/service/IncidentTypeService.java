package lk.ashan.routenetlkserverapllication.module.incident.service;


import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentTypeMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentTypeDto;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Incident Types.
 * Provides methods to retrieve incident type data.
 */
@Service
@RequiredArgsConstructor
public class IncidentTypeService {

    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentTypeMapper incidentTypeMapper;

    /**
     * Retrieves a list of all incident types.
     *
     * @return a list of {@link IncidentTypeDto} objects representing all incident types.
     */
    @Transactional(readOnly = true)
    public List<IncidentTypeDto> getIncidentTypes(){
       return incidentTypeMapper.toDtoList(incidentTypeRepository.findAll());
    }

}
