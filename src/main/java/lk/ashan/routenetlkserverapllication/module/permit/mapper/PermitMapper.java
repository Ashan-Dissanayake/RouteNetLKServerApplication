package lk.ashan.routenetlkserverapllication.module.permit.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.employee.dto.EmployeeCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        VehicleMapper.class, BranchMapper.class, PermitStatusMapper.class,
        ServiceTypeMapper.class, RouteMapper.class
})
public interface PermitMapper {
    PermitDetailResponseDto toDto(Permite permit);
    List<PermitDetailResponseDto> toDtoList(List<Permite> permits);

    Permite toEntity(PermitCreateRequestDto request);

}
