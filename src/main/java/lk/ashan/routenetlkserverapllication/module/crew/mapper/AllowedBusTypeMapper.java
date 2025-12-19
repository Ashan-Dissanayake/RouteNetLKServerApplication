package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Allowedbustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AllowedBusTypeMapper {
    AllowedBusTypeDto toDto(Allowedbustype allowedbustype);
    List<AllowedBusTypeDto> toDtoList(List<Allowedbustype> allowedbustypes);

}
