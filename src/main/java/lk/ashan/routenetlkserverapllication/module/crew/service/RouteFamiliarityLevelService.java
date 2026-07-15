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

/**
 * Service class for managing Route Familiarity Levels.
 * Provides methods to retrieve Route Familiarity Level data.
 */
@Service
@RequiredArgsConstructor
public class RouteFamiliarityLevelService {

    private final RouteFamiliarityLevelRepository routeFamiliarityLevelRepository;
    private final RouteFamiliarityLevelMapper routefamiliaritylevelMapper;

    /**
     * Retrieves all Route Familiarity Levels.
     *
     * @return a list of RouteFamiliarityLevelDto objects representing all Route Familiarity Levels.
     */
    @Transactional(readOnly = true)
    public List<RouteFamiliarityLevelDto> getRouteFamiliarityLevels() {
        return routefamiliaritylevelMapper.toDtoList(routeFamiliarityLevelRepository.findAll());
    }

    /**
     * Retrieves a Route Familiarity Level by its ID.
     *
     * @param id the ID of the Route Familiarity Level to retrieve.
     * @return the RouteFamiliarityLevel entity corresponding to the given ID.
     * @throws ResourceNotFoundException if no Route Familiarity Level is found with the given ID.
     */
    @Transactional(readOnly = true)
    public RouteFamiliarityLevel getById(Integer id) {
        return routeFamiliarityLevelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Gender not found"
                ));
    }

}
