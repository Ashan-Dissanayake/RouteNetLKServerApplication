package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.mapper;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {TripCrewAttendanceStatusMapper.class})
public interface TripCrewAttendanceMapper {
  TripCrewAttendanceDetailsResponseDto toDto(TripCrewAttendance tripCrewAttendance);
  List<TripCrewAttendanceDetailsResponseDto> toDtoList(List<TripCrewAttendance> tripCrewAttendances);
  TripCrewAttendance toEntity(TripCrewAttendanceCreateRequestDto crewAttendanceCreateRequestDto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "tocheckin",ignore = true)
  TripCrewAttendance updateEntityFromDto(
          TripCrewAttendanceUpdateRequestDto crewAttendanceUpdateRequestDto,
           @MappingTarget TripCrewAttendance entity
  );
}
