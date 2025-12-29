package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Licensecategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        AllowedBusTypeMapper.class
})
public interface LicenseCategoryMapper {
    LicenseCategoryDto toDto(Licensecategory licenseCategory);
    List<LicenseCategoryDto> toDtoList(List<Licensecategory> licenseCategories);
}
