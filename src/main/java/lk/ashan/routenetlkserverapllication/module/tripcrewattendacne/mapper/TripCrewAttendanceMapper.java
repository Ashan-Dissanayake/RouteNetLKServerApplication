package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.mapper;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {TripCrewAttendanceStatusMapper.class})
public interface TripCrewAttendanceMapper {
  TripCrewAttendanceDetailsResponseDto toDto(TripCrewAttendance tripCrewAttendance);
  List<TripCrewAttendanceDetailsResponseDto> toDtoList(List<TripCrewAttendance> tripCrewAttendances);
}
