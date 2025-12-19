package lk.ashan.routenetlkserverapllication.module.driver.mapper;

import lk.ashan.routenetlkserverapllication.module.driver.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.driver.model.Licensecategory;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        AllowedBusTypeMapper.class
})
public interface LicenseCategoryMapper {
    LicenseCategoryDto toDto(Licensecategory licenseCategory);
}
