package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationStatusMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing Incident Vehicle Allocation Statuses.
 * Provides methods to retrieve all statuses and find a specific status by name.
 */
@Service
@RequiredArgsConstructor
public class IncidentVehicleAllocationStatusService {

    private final IncidentVehicleAllocationStatusRepository incidentVehicleAllocationStatusRepository;
    private final IncidentVehicleAllocationStatusMapper incidentVehicleAllocationStatusMapper;

    /**
     * Retrieves all Incident Vehicle Allocation Statuses.
     *
     * @return a list of {@link IncidentVehicleAllocationStatusDto} representing all statuses.
     */
    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationStatusDto> getIncidentVehicleAllocationStatuses() {
        return incidentVehicleAllocationStatusMapper.toDtoList(incidentVehicleAllocationStatusRepository.findAll());
    }

    /**
     * Retrieves an Incident Vehicle Allocation Status by its name.
     *
     * @param name the name of the Incident Vehicle Allocation Status to retrieve.
     * @return the {@link IncidentVehicleAllocationStatus} with the specified name.
     * @throws ResourceNotFoundException if no status with the specified name is found.
     */
    @Transactional(readOnly = true)
    public IncidentVehicleAllocationStatus getByName(String name) {
        return incidentVehicleAllocationStatusRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Incident status '" + name + "' not found"
                ));
    }
}
