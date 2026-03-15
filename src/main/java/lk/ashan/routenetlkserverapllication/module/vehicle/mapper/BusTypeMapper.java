package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BusTypeMapper {
    BusTypeDto toDto(BusType bustype);
    List<BusTypeDto> toDtoList(List<BusType> allowedBusTypes);

}
