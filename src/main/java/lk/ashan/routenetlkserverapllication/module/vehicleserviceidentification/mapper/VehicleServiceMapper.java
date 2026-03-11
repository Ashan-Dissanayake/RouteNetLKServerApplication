package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto.VehicleServicePartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto.VehicleServicePartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto.VehicleServiceSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicepart;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface VehicleServiceMapper {
    VehicleServiceSummaryResponseDto toDto(Vehicleservice entity);
}
