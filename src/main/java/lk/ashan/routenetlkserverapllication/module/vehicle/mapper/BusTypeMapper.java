package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Bustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BusTypeMapper {
    BusTypeDto toDto(Bustype bustype);
    List<BusTypeDto> toDtoList(List<Bustype> allowedBusTypes);

}
