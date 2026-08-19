package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DepartmentDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Department;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper interface for converting Department entities to Department DTOs and vice versa.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {

    /**
     * Converts a Department entity to a DepartmentDto.
     *
     * @param department the Department entity to be converted
     * @return the corresponding DepartmentDto
     */
    DepartmentDto toDto(Department department);

    /**
     * Converts a list of Department entities to a list of DepartmentDto objects.
     *
     * @param departments the list of Department entities to be converted
     * @return the corresponding list of DepartmentDto objects
     */
    List<DepartmentDto> toDtoList(List<Department> departments);

}
