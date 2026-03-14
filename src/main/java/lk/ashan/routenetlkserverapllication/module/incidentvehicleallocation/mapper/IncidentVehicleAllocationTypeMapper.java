package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationTypeDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentVehicleAllocationTypeMapper {
    IncidentVehicleAllocationTypeDto toDto(IncidentVehicleAllocationType incidentVehicleAllocationType);
    List<IncidentVehicleAllocationTypeDto> toDtoList(List<IncidentVehicleAllocationType> incidentVehicleAllocationTypes);

}
