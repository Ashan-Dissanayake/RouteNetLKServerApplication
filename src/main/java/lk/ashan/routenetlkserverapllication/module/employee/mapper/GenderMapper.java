package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Gender;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting Gender entities to Gender DTOs and vice versa.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GenderMapper {

    /**
     * Converts a Gender entity to a GenderDto.
     *
     * @param gender the Gender entity to be converted
     * @return the converted GenderDto
     */
    GenderDto toDto(Gender gender);

    /**
     * Converts a list of Gender entities to a list of GenderDto objects.
     *
     * @param genders the list of Gender entities to be converted
     * @return the list of converted GenderDto objects
     */
    List<GenderDto> toDtoList(List<Gender> genders);

}
