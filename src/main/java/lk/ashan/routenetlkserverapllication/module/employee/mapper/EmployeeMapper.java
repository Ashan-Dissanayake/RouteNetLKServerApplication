package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeMapper {

    EmployeeDetailResponseDto toDto(Employee employeeDetailResponse);

    List<EmployeeDetailResponseDto> toDtoList(List<Employee> employeeDetailResponses);

    Employee toEntity(EmployeeCreateRequestDto request);
    Employee toEntity(EmployeeUpdateRequestDto request);



}
