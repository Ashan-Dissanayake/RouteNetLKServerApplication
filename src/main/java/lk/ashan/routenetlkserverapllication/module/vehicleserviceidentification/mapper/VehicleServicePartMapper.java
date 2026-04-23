package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServicePart;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                VehicleServiceMapper.class,
                PartMapper.class
        }
)
public interface VehicleServicePartMapper {

    VehicleServicePart toEntity(VehicleServicePartCreateRequestDto dto);
    VehicleServicePart toEntity(VehicleServicePartUpdateRequestDto dto);

    VehicleServicePartDetailResponseDto toDto(VehicleServicePart entity);

    List<VehicleServicePartDetailResponseDto> toDtoList(List<VehicleServicePart> entities);

    @Mapping(target = "id", ignore = true) // don't override ID
    @Mapping(target = "vehicleservice", ignore = true) // optionally ignore if not updatable
    VehicleServicePart updateEntityFromDto(VehicleServicePartUpdateRequestDto dto, @MappingTarget VehicleServicePart entity);

}
