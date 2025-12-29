package lk.ashan.routenetlkserverapllication.module.crew.mapper;

import lk.ashan.routenetlkserverapllication.module.crew.dto.ConductorDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Conductor;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        EmployeeMapper.class,RouteFamiliarityLevelMapper.class, CrewStatusMapper.class
})
public interface ConductorMapper {

    List<ConductorDetailResponseDto> toDtoList(List<Conductor> conductors);
    ConductorDetailResponseDto toDto(Conductor conductor);

}
