package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.LicenseCategory;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.BusTypeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


/**
 * Mapper interface for converting between `LicenseCategory` entities and `LicenseCategoryDto` objects.
 * Utilizes `BusTypeMapper` for related mappings.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        BusTypeMapper.class
})
public interface LicenseCategoryMapper {

    /**
     * Converts a `LicenseCategory` entity to a `LicenseCategoryDto`.
     *
     * @param licenseCategory the `LicenseCategory` entity to be converted
     * @return the converted `LicenseCategoryDto`
     */
    LicenseCategoryDto toDto(LicenseCategory licenseCategory);

    /**
     * Converts a list of `LicenseCategory` entities to a list of `LicenseCategoryDto` objects.
     *
     * @param licenseCategories the list of `LicenseCategory` entities to be converted
     * @return the list of converted `LicenseCategoryDto` objects
     */
    List<LicenseCategoryDto> toDtoList(List<LicenseCategory> licenseCategories);
}
