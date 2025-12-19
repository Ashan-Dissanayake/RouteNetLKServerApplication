package lk.ashan.routenetlkserverapllication.module.driver.service;

import lk.ashan.routenetlkserverapllication.module.driver.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.driver.mapper.RouteFamiliarityLevelMapper;
import lk.ashan.routenetlkserverapllication.module.driver.repository.RouteFamiliarityLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteFamiliarityLevelService {

    private final RouteFamiliarityLevelRepository routeFamiliarityLevelRepository;
    private final RouteFamiliarityLevelMapper routefamiliaritylevelMapper;

    public List<RouteFamiliarityLevelDto> getRouteFamiliarityLevels(){
       return routefamiliaritylevelMapper.toDtoList(routeFamiliarityLevelRepository.findAll());
    }

}
