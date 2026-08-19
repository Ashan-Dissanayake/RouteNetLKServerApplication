package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeStatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

/**
 * Mapper interface for converting between EmployeeStatus entity and EmployeeStatusDto.
 * Utilizes MapStruct for automatic mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeStatusMapper {

    /**
     * Converts an EmployeeStatus entity to an EmployeeStatusDto.
     *
     * @param employeestatus the EmployeeStatus entity to be converted
     * @return the converted EmployeeStatusDto
     */
    EmployeeStatusDto toDto(EmployeeStatus employeestatus);

    /**
     * Converts a list of EmployeeStatus entities to a list of EmployeeStatusDto.
     *
     * @param employeestatusses the list of EmployeeStatus entities to be converted
     * @return the list of converted EmployeeStatusDto
     */
    List<EmployeeStatusDto> toDtoList(List<EmployeeStatus> employeestatusses);

}
