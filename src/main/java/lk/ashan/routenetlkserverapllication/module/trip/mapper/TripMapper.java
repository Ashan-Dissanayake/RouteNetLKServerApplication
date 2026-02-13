package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        TripTypeMapper.class, TripStatusMapper.class, TripVehicleOverrideMapper.class, OriginTerminalMapper.class
})
public interface TripMapper {
  TripDetailResponseDto toDto(Trip trip);
  List<TripDetailResponseDto> toDetailList(List<Trip> trips);

  Trip toEntity(TripCreateRequestDto requestDto);
  Trip toEntity(TripUpdateRequestDto requestDto);

}
