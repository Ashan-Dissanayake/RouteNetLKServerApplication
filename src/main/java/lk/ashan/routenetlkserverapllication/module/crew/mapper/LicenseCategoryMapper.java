package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Licensecategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LicenseCategoryMapper {
    LicenseCategoryDto toDto(Licensecategory licensecategory);
}
