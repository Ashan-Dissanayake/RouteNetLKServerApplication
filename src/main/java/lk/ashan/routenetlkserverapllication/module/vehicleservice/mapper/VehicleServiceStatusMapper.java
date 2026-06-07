package lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceStatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleServiceStatusMapper {
    VehicleServiceStatusDto toDto(VehicleServiceStatus vehicleServiceStatus);
    List<VehicleServiceStatusDto> toDtoList(List<VehicleServiceStatus> vehicleServiceStatus);
}
