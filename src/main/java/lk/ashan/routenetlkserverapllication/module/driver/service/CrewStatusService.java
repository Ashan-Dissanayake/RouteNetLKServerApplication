package lk.ashan.routenetlkserverapllication.module.driver.service;

import lk.ashan.routenetlkserverapllication.module.driver.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.CrewStatusMapper;
import lk.ashan.routenetlkserverapllication.module.driver.repository.CrewStatusRepository;
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
