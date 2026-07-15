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

/**
 * Service class for managing Crew Status operations.
 */
@Service
@RequiredArgsConstructor
public class CrewStatusService {

    private final CrewStatusRepository crewStatusRepository;
    private final CrewStatusMapper crewStatusMapper;

    /**
     * Retrieves all Crew Statuses.
     *
     * @return a list of CrewStatusDto objects representing all crew statuses.
     */
    @Transactional(readOnly = true)
    public List<CrewStatusDto> getCrewStatuses() {
        return crewStatusMapper.toDtoList(crewStatusRepository.findAll());
    }

    /**
     * Retrieves a Crew Status by its ID.
     *
     * @param id the ID of the Crew Status to retrieve.
     * @return the CrewStatus entity corresponding to the given ID.
     * @throws ResourceNotFoundException if no Crew Status is found with the given ID.
     */
    @Transactional(readOnly = true)
    public CrewStatus getById(Integer id) {
        return crewStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Status not found"
                ));
    }
}
