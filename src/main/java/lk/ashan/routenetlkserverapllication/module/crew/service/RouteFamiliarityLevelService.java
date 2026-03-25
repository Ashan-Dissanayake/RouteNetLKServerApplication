package lk.ashan.routenetlkserverapllication.module.crew.service;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.mapper.RouteFamiliarityLevelMapper;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.crew.repository.RouteFamiliarityLevelRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteFamiliarityLevelService {

    private final RouteFamiliarityLevelRepository routeFamiliarityLevelRepository;
    private final RouteFamiliarityLevelMapper routefamiliaritylevelMapper;

    @Transactional(readOnly = true)
    public List<RouteFamiliarityLevelDto> getRouteFamiliarityLevels(){
       return routefamiliaritylevelMapper.toDtoList(routeFamiliarityLevelRepository.findAll());
    }

    @Transactional(readOnly = true)
    public RouteFamiliarityLevel getById(Integer id) {
        return routeFamiliarityLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gender not found"
                ));
    }

}
