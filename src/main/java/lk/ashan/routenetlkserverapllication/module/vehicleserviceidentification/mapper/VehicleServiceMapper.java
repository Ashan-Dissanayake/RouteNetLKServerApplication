package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServiceSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleServiceMapper {
    VehicleServiceSummaryResponseDto toDto(VehicleService entity);
}
