package lk.ashan.routenetlkserverapllication.module.incident.mapper;

import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface IncidentStatusMapper {
    IncidentStatusDto toDto(Incidentstatus incidentStatus);
    List<IncidentStatusDto> toDtoList(List<Incidentstatus> incidentStatuses);
}
