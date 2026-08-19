package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between IncidentVehicleAllocationStatus entities and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentVehicleAllocationStatusMapper {

    /**
     * Converts an IncidentVehicleAllocationStatus entity to its corresponding DTO.
     *
     * @param incidentVehicleAllocationStatus the entity to be converted
     * @return the converted IncidentVehicleAllocationStatusDto
     */
    IncidentVehicleAllocationStatusDto toDto(IncidentVehicleAllocationStatus incidentVehicleAllocationStatus);

    /**
     * Converts a list of IncidentVehicleAllocationStatus entities to a list of corresponding DTOs.
     *
     * @param incidentVehicleAllocationStatuses the list of entities to be converted
     * @return the list of converted IncidentVehicleAllocationStatusDto objects
     */
    List<IncidentVehicleAllocationStatusDto> toDtoList(List<IncidentVehicleAllocationStatus> incidentVehicleAllocationStatuses);

}
