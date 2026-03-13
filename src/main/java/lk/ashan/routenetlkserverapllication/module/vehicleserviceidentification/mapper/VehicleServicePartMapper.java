package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServicePartUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicepart;
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

    Vehicleservicepart toEntity(VehicleServicePartCreateRequestDto dto);
    Vehicleservicepart toEntity(VehicleServicePartUpdateRequestDto dto);

    VehicleServicePartDetailResponseDto toDto(Vehicleservicepart entity);

    List<VehicleServicePartDetailResponseDto> toDtoList(List<Vehicleservicepart> entities);

    @Mapping(target = "id", ignore = true) // don't override ID
    @Mapping(target = "vehicleservice", ignore = true) // optionally ignore if not updatable
    Vehicleservicepart updateEntityFromDto(VehicleServicePartUpdateRequestDto dto, @MappingTarget Vehicleservicepart entity);

}
