package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocationstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentVehicleAllocationStatusMapper {
    IncidentVehicleAllocationStatusDto toDto(Incidentvehicleallocationstatus incidentVehicleAllocationStatus);
    List<IncidentVehicleAllocationStatusDto> toDtoList(List<Incidentvehicleallocationstatus> incidentVehicleAllocationStatuses);

}
