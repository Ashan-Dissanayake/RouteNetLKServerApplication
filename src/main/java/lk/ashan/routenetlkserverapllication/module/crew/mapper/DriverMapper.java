package lk.ashan.routenetlkserverapllication.module.crew.mapper;


import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        EmployeeMapper.class, AllowedBusTypeMapper.class, LicenseCategoryMapper.class,
        RouteFamiliarityLevelMapper.class, CrewStatusMapper.class
})
public interface DriverMapper {

    List<DriverDetailResponseDto> toDtoList(List<Driver> drivers);
}
