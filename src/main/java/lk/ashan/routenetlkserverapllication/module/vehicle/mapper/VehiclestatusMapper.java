package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehiclestatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehiclestatusMapper {

    VehiclestatusDto toDto(Vehiclestatus vehiclestatus);
    List<VehiclestatusDto> toDtoList(List<Vehiclestatus> vehiclestatuses);

}
