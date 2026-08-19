package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper interface for converting between Employee entities and DTOs.
 * Utilizes MapStruct for object mapping.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        DepartmentMapper.class, DesignationMapper.class, EmployeeStatusMapper.class,
        EmployeeTypeMapper.class, GenderMapper.class, BranchMapper.class
})
public interface EmployeeMapper {

    /**
     * Converts an Employee entity to an EmployeeDetailResponseDto.
     *
     * @param employeeDetailResponse the Employee entity to convert
     * @return the converted EmployeeDetailResponseDto
     */
    EmployeeDetailResponseDto toDto(Employee employeeDetailResponse);

    /**
     * Converts a list of Employee entities to a list of EmployeeDetailResponseDto.
     *
     * @param employeeDetailResponses the list of Employee entities to convert
     * @return the list of converted EmployeeDetailResponseDto
     */
    List<EmployeeDetailResponseDto> toDtoList(List<Employee> employeeDetailResponses);

    /**
     * Converts a list of Employee entities to a list of EmployeeSummaryDto.
     *
     * @param employees the list of Employee entities to convert
     * @return the list of converted EmployeeSummaryDto
     */
    List<EmployeeSummaryDto> toSummaryDetailList(List<Employee> employees);

    /**
     * Converts an EmployeeCreateRequestDto to an Employee entity.
     *
     * @param request the EmployeeCreateRequestDto to convert
     * @return the converted Employee entity
     */
    Employee toEntity(EmployeeCreateRequestDto request);

    /**
     * Updates an existing Employee entity with data from an EmployeeUpdateRequestDto.
     * Fields specified in the @Mapping annotations are ignored during the update.
     *
     * @param dto    the EmployeeUpdateRequestDto containing updated data
     * @param entity the Employee entity to update
     * @return the updated Employee entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "employeetype", ignore = true)
    @Mapping(target = "employeestatus", ignore = true)
    @Mapping(target = "designation", ignore = true)
    @Mapping(target = "department", ignore = true)
    Employee updateEntityFromDto(EmployeeUpdateRequestDto dto, @MappingTarget Employee entity);

}
