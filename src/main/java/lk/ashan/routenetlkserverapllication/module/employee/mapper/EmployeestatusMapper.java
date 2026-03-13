package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.EmployeestatusDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employeestatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeestatusMapper {

    EmployeestatusDto toDto(Employeestatus employeestatus);
    List<EmployeestatusDto> toDtoList(List<Employeestatus> employeestatuss);


}
