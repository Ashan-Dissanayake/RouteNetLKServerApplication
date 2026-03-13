package lk.ashan.routenetlkserverapllication.module.trip.mapper;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripVehicleOverrideDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripvehicleoverride;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        OverrideStatusMapper.class
})
public interface TripVehicleOverrideMapper {
    Tripvehicleoverride toEntity(TripVehicleOverrideDto tripvehicleoverrideDto);
    TripVehicleOverrideDto toDto(Tripvehicleoverride tripvehicleoverride);
}
