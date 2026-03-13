package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.Incidentvehicleallocation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
uses = {
        IncidentVehicleAllocationStatusMapper.class, IncidentVehicleAllocationTypeMapper.class
})
public interface IncidentVehicleAllocationMapper {
    IncidentVehicleAllocationDetailsResponseDto toDto(Incidentvehicleallocation incidentVehicleAllocation);
    List<IncidentVehicleAllocationDetailsResponseDto> toDtoList(List<Incidentvehicleallocation> incidentVehicleAllocations);
    Incidentvehicleallocation toEntity(IncidentVehicleAllocationCreateRequestDto incidentVehicleAllocationCreateRequestDto);

}
