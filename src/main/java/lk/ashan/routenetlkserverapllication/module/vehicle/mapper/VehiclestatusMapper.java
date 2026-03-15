package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehiclestatusMapper {

    VehiclestatusDto toDto(VehicleStatus vehiclestatus);
    List<VehiclestatusDto> toDtoList(List<VehicleStatus> vehicleStatuses);

}
