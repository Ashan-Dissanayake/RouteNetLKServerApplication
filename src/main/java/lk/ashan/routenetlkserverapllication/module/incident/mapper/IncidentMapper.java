package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.tripexecution.mapper.TripExecutionMapper;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {IncidentStatusMapper.class, IncidentTypeMapper.class, TripExecutionMapper.class}
)
public interface IncidentMapper {
    IncidentDetailResponseDto toDto(Incident incident);
    List<IncidentDetailResponseDto> toDtoList(List<Incident> incidents);

    Incident toEntity(IncidentCreateRequestDto createRequestDto);

    @Mapping(target = "id", ignore = true)
    Incident updateEntityFromDto(IncidentUpdateRequestDto dto, @MappingTarget Incident entity);

    @Mapping(target = "name",expression = "java(getIncidentName(incident))")
    @Mapping(target = "regionalareaId", source = "regionalarea.id")
    IncidentSummaryDto toSummaryDto(Incident incident);

    List<IncidentSummaryDto> toSummaryDtoList(List<Incident> incidents);

    default String getIncidentName(Incident incident) {
        String vehicleNumber = incident.getTripexecution().getVehicle().getNumber();
        String incidentType = incident.getIncidenttype().getName();

        return String.format("%s - %s", vehicleNumber, incidentType);
    }
}
