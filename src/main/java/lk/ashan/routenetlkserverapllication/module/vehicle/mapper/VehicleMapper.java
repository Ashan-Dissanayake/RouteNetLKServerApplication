package lk.ashan.routenetlkserverapllication.module.vehicle.mapper;

import lk.ashan.routenetlkserverapllication.module.branch.mapper.BranchMapper;
import lk.ashan.routenetlkserverapllication.module.employee.mapper.EmployeeMapper;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,uses = {
        ConditionrateMapper.class,EmployeeMapper.class,VehiclestatusMapper.class,
        FueltypeMapper.class, ModelMapper.class, MakeMapper.class, BranchMapper.class, BusTypeMapper.class
})
public interface VehicleMapper {

    VehicleDetailResponseDto toDto(Vehicle vehicle);
    List<VehicleDetailResponseDto> toDtoList(List<Vehicle> vehicleDetailResponses);

    Vehicle toEntity(VehicleCreateRequestDto requestDto);
    Vehicle toEntity(VehicleUpdateRequestDto requestDto);




}
