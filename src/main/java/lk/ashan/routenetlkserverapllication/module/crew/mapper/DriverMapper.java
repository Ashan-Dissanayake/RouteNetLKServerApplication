package lk.ashan.routenetlkserverapllication.module.crew.mapper;


import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        EmployeeMapper.class,LicenseCategoryMapper.class,
        RouteFamiliarityLevelMapper.class, CrewStatusMapper.class
})
public interface DriverMapper {

    List<DriverDetailResponseDto> toDtoList(List<Driver> drivers);
    DriverDetailResponseDto toDto(Driver driver);

    Driver toEntity(DriverCreateRequestDto driverCreateRequestDto);

    Driver toEntity(DriverUpdateRequestDto driverUpdateRequestDto);

    @Mapping(target = "id", ignore = true)
    Driver updateEntityFromDto(DriverUpdateRequestDto dto, @MappingTarget Driver entity);

}
