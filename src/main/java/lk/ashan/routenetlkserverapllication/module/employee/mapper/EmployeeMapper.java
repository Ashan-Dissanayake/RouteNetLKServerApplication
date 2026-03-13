package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        DepartmentMapper.class, DesignationMapper.class,EmployeestatusMapper.class,
        EmployeetypeMapper.class,GenderMapper.class, BranchMapper.class
})
public interface EmployeeMapper {

    EmployeeDetailResponseDto toDto(Employee employeeDetailResponse);

    List<EmployeeDetailResponseDto> toDtoList(List<Employee> employeeDetailResponses);

    List<EmployeeSummaryResponseDto> toSummaryDetailList(List<Employee> employees);

    Employee toEntity(EmployeeCreateRequestDto request);
    Employee toEntity(EmployeeUpdateRequestDto request);
}
