package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper.IncidentVehicleAllocationStatusMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentVehicleAllocationStatusService {

    private final IncidentVehicleAllocationStatusRepository incidentVehicleAllocationStatusRepository;
    private final IncidentVehicleAllocationStatusMapper incidentVehicleAllocationStatusMapper;

    @Transactional(readOnly = true)
    public List<IncidentVehicleAllocationStatusDto> getIncidentVehicleAllocationStatuses(){
        return incidentVehicleAllocationStatusMapper.toDtoList(incidentVehicleAllocationStatusRepository.findAll());
    }



}
