package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.dto.DepartmentDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.Department;
import lk.ashan.routenetlkserverapllication.module.employee.model.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.dto.DesignationDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DesignationMapper {

    DesignationDto toDto(Designation designation);
    List<DesignationDto> toDtoList(List<Designation> designations);


}
