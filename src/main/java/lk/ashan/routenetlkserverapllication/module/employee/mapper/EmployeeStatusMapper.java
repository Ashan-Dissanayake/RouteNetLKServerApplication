package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeeStatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeeStatusMapper {

    EmployeeStatusDto toDto(EmployeeStatus employeestatus);
    List<EmployeeStatusDto> toDtoList(List<EmployeeStatus> employeestatusses);


}
