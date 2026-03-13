package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.CrewStatusMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.CrewStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrewStatusService {

    private final CrewStatusRepository crewStatusRepository;
    private final CrewStatusMapper crewStatusMapper;

    public List<CrewStatusDto> getCrewStatuses(){
       return crewStatusMapper.toDtoList(crewStatusRepository.findAll());
    }

}
