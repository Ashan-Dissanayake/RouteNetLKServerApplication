package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Bustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AllowedBusTypeMapper {
    AllowedBusTypeDto toDto(Bustype bustype);
    List<AllowedBusTypeDto> toDtoList(List<Bustype> allowedBusTypes);

}
