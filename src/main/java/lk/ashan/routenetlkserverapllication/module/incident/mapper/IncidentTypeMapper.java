package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentTypeDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper interface for converting between IncidentType entities and DTOs.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentTypeMapper {

    /**
     * Converts an IncidentType entity to an IncidentTypeDto.
     *
     * @param incidentType the IncidentType entity to be converted
     * @return the converted IncidentTypeDto
     */
    IncidentTypeDto toDto(IncidentType incidentType);

    /**
     * Converts a list of IncidentType entities to a list of IncidentTypeDto objects.
     *
     * @param incidentTypes the list of IncidentType entities to be converted
     * @return the list of converted IncidentTypeDto objects
     */
    List<IncidentTypeDto> toDtoList(List<IncidentType> incidentTypes);
}
