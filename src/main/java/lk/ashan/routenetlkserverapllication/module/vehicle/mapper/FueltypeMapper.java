package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.FueltypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Fueltype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FueltypeMapper {

    FueltypeDto toDto(Fueltype fueltype);
    List<FueltypeDto> toDtoList(List<Fueltype> fueltypes);

}
