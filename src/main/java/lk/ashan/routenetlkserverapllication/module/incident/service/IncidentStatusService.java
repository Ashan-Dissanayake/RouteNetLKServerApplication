package lk.ashan.routenetlkserverapllication.module.incident.service;

import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentStatusMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentStatusService {

    private final IncidentStatusRepository incidentStatusRepository;
    private final IncidentStatusMapper incidentStatusMapper;

    @Transactional(readOnly = true)
    public List<IncidentStatusDto> getIncidentStatuses(){
        return incidentStatusMapper.toDtoList(incidentStatusRepository.findAll());
    }

    @Transactional(readOnly = true)
    public IncidentStatus getByName(String name) {
        return incidentStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Incident status '" + name + "' not found"
                ));
    }

}
