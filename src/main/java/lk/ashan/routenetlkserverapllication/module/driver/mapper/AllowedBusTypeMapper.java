package lk.ashan.routenetlkserverapllication.module.driver.mapper;

import lk.ashan.routenetlkserverapllication.module.driver.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.driver.model.Allowedbustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AllowedBusTypeMapper {
    AllowedBusTypeDto toDto(Allowedbustype allowedbustype);
    List<AllowedBusTypeDto> toDtoList(List<Allowedbustype> allowedBusTypes);

}
