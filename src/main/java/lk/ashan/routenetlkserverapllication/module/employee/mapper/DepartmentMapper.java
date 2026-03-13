package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DepartmentDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Department;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DepartmentMapper {

    DepartmentDto toDto(Department department);
    List<DepartmentDto> toDtoList(List<Department> departments);

}
