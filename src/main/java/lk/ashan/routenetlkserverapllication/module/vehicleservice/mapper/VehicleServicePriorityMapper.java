package lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePriorityDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePriority;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleServicePriorityMapper {
    VehicleServicePriorityDto toDto(VehicleServicePriority vehicleServicePriority);
    List<VehicleServicePriorityDto> toDtoList(List<VehicleServicePriority> vehicleServicePriority);
}
