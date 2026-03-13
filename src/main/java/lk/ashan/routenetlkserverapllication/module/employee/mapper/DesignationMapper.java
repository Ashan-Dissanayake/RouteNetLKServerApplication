package lk.ashan.routenetlkserverapllication.module.employee.mapper;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Designation;
import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DesignationMapper {

    DesignationDto toDto(Designation designation);
    List<DesignationDto> toDtoList(List<Designation> designations);


}
