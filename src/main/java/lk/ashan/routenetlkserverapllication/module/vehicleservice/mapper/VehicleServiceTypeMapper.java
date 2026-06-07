package lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleServiceTypeMapper {
    VehicleServiceTypeDto toDto(VehicleServiceType vehicleServiceType);
    List<VehicleServiceTypeDto> toDtoList(List<VehicleServiceType> vehicleServiceTypes);
}
