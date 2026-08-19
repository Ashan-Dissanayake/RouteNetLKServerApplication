package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeTypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between EmployeeType entities and EmployeeTypeDto objects.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeTypeMapper {

    /**
     * Converts an EmployeeType entity to an EmployeeTypeDto.
     *
     * @param employeetype the EmployeeType entity to be converted
     * @return the converted EmployeeTypeDto
     */
    EmployeeTypeDto toDto(EmployeeType employeetype);

    /**
     * Converts a list of EmployeeType entities to a list of EmployeeTypeDto objects.
     *
     * @param employeeTypes the list of EmployeeType entities to be converted
     * @return the list of converted EmployeeTypeDto objects
     */
    List<EmployeeTypeDto> toDtoList(List<EmployeeType> employeeTypes);

}
