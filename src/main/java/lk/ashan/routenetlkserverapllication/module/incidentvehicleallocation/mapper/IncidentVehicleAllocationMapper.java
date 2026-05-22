package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        IncidentVehicleAllocationStatusMapper.class, VehicleMapper.class, BranchMapper.class, IncidentMapper.class
})
public interface IncidentVehicleAllocationMapper {

    IncidentVehicleAllocationDetailsResponseDto toDto(IncidentVehicleAllocation incidentVehicleAllocation);

    List<IncidentVehicleAllocationDetailsResponseDto> toDtoList(List<IncidentVehicleAllocation> incidentVehicleAllocations);

    IncidentVehicleAllocation toEntity(IncidentVehicleAllocationCreateRequestDto incidentVehicleAllocationCreateRequestDto);
}
