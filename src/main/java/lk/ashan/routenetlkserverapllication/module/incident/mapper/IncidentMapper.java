package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.trip.mapper.TripMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
    uses = {
            IncidentStatusMapper.class,
            IncidentTypeMapper.class,
            TripMapper.class
    }
)

public interface IncidentMapper {
    IncidentDetailResponseDto toDto(Incident incident);
    List<IncidentDetailResponseDto> toDtoList(List<Incident> incidents);
    Incident toEntity(IncidentCreateRequestDto createRequestDto);
}
