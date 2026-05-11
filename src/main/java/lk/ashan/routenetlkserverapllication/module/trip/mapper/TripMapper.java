package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.permit.mapper.PermitMapper;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        TripTypeMapper.class, TripStatusMapper.class, OriginTerminalMapper.class,
        PermitMapper.class,OpCalenderMapper.class
})
public interface TripMapper {
  TripDetailResponseDto toDto(Trip trip);
  List<TripDetailResponseDto> toDetailList(List<Trip> trips);

  Trip toEntity(TripCreateRequestDto requestDto);
  Trip toEntity(TripUpdateRequestDto requestDto);

}
