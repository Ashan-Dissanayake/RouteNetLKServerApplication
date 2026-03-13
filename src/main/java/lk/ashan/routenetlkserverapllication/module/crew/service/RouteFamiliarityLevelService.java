package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.RouteFamiliarityLevelMapper;
import lk.ashan.routenetlkserverapllication.module.crew.repository.RouteFamiliarityLevelRepository;
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
