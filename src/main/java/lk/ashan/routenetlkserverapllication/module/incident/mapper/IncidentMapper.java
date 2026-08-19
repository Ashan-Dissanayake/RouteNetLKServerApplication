package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.tripexecution.mapper.TripExecutionMapper;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between Incident entities and DTOs.
 * Utilizes MapStruct for mapping configurations and custom mappings.
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {IncidentStatusMapper.class, IncidentTypeMapper.class, TripExecutionMapper.class}
)
public interface IncidentMapper {

    /**
     * Converts an Incident entity to an IncidentDetailResponseDto.
     *
     * @param incident the Incident entity to convert
     * @return the converted IncidentDetailResponseDto
     */
    IncidentDetailResponseDto toDto(Incident incident);

    /**
     * Converts a list of Incident entities to a list of IncidentDetailResponseDto.
     *
     * @param incidents the list of Incident entities to convert
     * @return the list of converted IncidentDetailResponseDto
     */
    List<IncidentDetailResponseDto> toDtoList(List<Incident> incidents);

    /**
     * Converts an IncidentCreateRequestDto to an Incident entity.
     *
     * @param createRequestDto the IncidentCreateRequestDto to convert
     * @return the converted Incident entity
     */
    Incident toEntity(IncidentCreateRequestDto createRequestDto);

    /**
     * Updates an existing Incident entity with values from an IncidentUpdateRequestDto.
     * The ID field is ignored during the update.
     *
     * @param dto the IncidentUpdateRequestDto containing updated values
     * @param entity the existing Incident entity to update
     * @return the updated Incident entity
     */
    @Mapping(target = "id", ignore = true)
    Incident updateEntityFromDto(IncidentUpdateRequestDto dto, @MappingTarget Incident entity);

    /**
     * Converts an Incident entity to an IncidentSummaryDto.
     * Includes custom mappings for the name and regional area ID fields.
     *
     * @param incident the Incident entity to convert
     * @return the converted IncidentSummaryDto
     */
    @Mapping(target = "name", expression = "java(getIncidentName(incident))")
    @Mapping(target = "regionalareaId", source = "regionalarea.id")
    IncidentSummaryDto toSummaryDto(Incident incident);

    /**
     * Converts a list of Incident entities to a list of IncidentSummaryDto.
     *
     * @param incidents the list of Incident entities to convert
     * @return the list of converted IncidentSummaryDto
     */
    List<IncidentSummaryDto> toSummaryDtoList(List<Incident> incidents);

    /**
     * Generates a formatted name for an Incident entity.
     * Combines the vehicle number and incident type name.
     *
     * @param incident the Incident entity to generate the name for
     * @return the formatted name as a String
     */
    default String getIncidentName(Incident incident) {
        String vehicleNumber = incident.getTripexecution().getVehicle().getNumber();
        String incidentType = incident.getIncidenttype().getName();

        return String.format("%s - %s", vehicleNumber, incidentType);
    }
}
