package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.CrewStatusMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import lk.ashan.routenetlkserverapllication.module.crew.repository.CrewStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewStatusService {

    private final CrewStatusRepository crewStatusRepository;
    private final CrewStatusMapper crewStatusMapper;

    @Transactional(readOnly = true)
    public List<CrewStatusDto> getCrewStatuses(){
       return crewStatusMapper.toDtoList(crewStatusRepository.findAll());
    }


    @Transactional(readOnly = true)
    public CrewStatus getById(Integer id) {
        return crewStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }
}
