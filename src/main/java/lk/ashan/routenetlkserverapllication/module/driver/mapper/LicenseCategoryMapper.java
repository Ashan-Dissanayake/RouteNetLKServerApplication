package lk.ashan.routenetlkserverapllication.module.driver.mapper;

import lk.ashan.routenetlkserverapllication.module.driver.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.driver.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.driver.model.Allowedbustype;
import lk.ashan.routenetlkserverapllication.module.driver.model.Licensecategory;
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
