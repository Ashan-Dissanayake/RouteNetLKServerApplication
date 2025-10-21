package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeetypeDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeetype;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EmployeetypeMapper {

    EmployeetypeDto toDto(Employeetype employeetype);
    List<EmployeetypeDto> toDtoList(List<Employeetype> employeetypes);


}
