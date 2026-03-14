package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentVehicleAllocationStatusMapper {
    IncidentVehicleAllocationStatusDto toDto(IncidentVehicleAllocationStatus incidentVehicleAllocationStatus);
    List<IncidentVehicleAllocationStatusDto> toDtoList(List<IncidentVehicleAllocationStatus> incidentVehicleAllocationStatuses);

}
