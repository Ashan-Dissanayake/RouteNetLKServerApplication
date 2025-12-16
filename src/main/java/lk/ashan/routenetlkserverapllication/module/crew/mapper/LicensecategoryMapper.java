package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.LicensecategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Licensecategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LicensecategoryMapper {
    LicensecategoryDto toDto(Licensecategory licensecategory);
}
