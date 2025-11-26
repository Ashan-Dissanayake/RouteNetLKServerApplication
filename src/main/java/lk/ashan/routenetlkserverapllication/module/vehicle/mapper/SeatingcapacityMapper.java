package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.SeatingcapacityDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Seatingcapacity;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SeatingcapacityMapper {

    SeatingcapacityDto toDto(Seatingcapacity seatingcapacity);
    List<SeatingcapacityDto> toDtoList(List<Seatingcapacity> seatingcapacity);

}
