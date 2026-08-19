package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting between Designation entities and DesignationDto objects.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DesignationMapper {

    /**
     * Converts a Designation entity to a DesignationDto.
     *
     * @param designation the Designation entity to be converted
     * @return the converted DesignationDto
     */
    DesignationDto toDto(Designation designation);

    /**
     * Converts a list of Designation entities to a list of DesignationDto objects.
     *
     * @param designations the list of Designation entities to be converted
     * @return the list of converted DesignationDto objects
     */
    List<DesignationDto> toDtoList(List<Designation> designations);

}
