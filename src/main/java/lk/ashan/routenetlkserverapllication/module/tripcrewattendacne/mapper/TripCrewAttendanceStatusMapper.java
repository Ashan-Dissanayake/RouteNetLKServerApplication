package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.mapper;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.CrewAttendanceStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripCrewAttendanceStatusMapper {
    CrewAttendanceStatusDto toDto(CrewAttendanceStatus crewAttendanceStatus);
    List<CrewAttendanceStatusDto> toDtoList(List<CrewAttendanceStatus> crewAttendanceStatuses);
    CrewAttendanceStatus toEntity(CrewAttendanceStatusDto crewAttendanceStatusDto);
}
