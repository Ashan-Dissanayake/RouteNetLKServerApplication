package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ConditionrateMapper.class,EmployeeMapper.class,VehiclestatusMapper.class,
        FueltypeMapper.class, ServicetypeMapper.class, MakeMapper.class, BranchMapper.class
})
public interface VehicleMapper {

    VehicleDetailResponseDto toDto(Vehicle vehicleDetailResponse);
    List<VehicleDetailResponseDto> toDtoList(List<Vehicle> vehicleDetailResponses);

    Vehicle toEntity(VehicleCreateRequestDto requestDto);


}
