package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting IncidentStatus entities to DTOs and vice versa.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentStatusMapper {

    /**
     * Converts an IncidentStatus entity to an IncidentStatusDto.
     *
     * @param incidentStatus the IncidentStatus entity to be converted
     * @return the converted IncidentStatusDto
     */
    IncidentStatusDto toDto(IncidentStatus incidentStatus);

    /**
     * Converts a list of IncidentStatus entities to a list of IncidentStatusDto objects.
     *
     * @param incidentStatuses the list of IncidentStatus entities to be converted
     * @return the list of converted IncidentStatusDto objects
     */
    List<IncidentStatusDto> toDtoList(List<IncidentStatus> incidentStatuses);
}
