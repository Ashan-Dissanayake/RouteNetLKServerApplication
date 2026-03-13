package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationTypeDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.Incidentvehicleallocationtype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentVehicleAllocationTypeMapper {
    IncidentVehicleAllocationTypeDto toDto(Incidentvehicleallocationtype incidentVehicleAllocationType);
    List<IncidentVehicleAllocationTypeDto> toDtoList(List<Incidentvehicleallocationtype> incidentVehicleAllocationTypes);

}
