package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        TripTypeMapper.class, DirectionMapper.class, TripStatusMapper.class, TripVehicleOverrideMapper.class,
})
public interface TripMapper {
  TripDetailResponseDto toDto(Trip trip);
  List<TripDetailResponseDto> toDetailList(List<Trip> tripes);
}
