package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.AllowedbustypeDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Allowedbustype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AllowedbustypeMapper {
    AllowedbustypeDto toDto(Allowedbustype allowedbustype);
}
