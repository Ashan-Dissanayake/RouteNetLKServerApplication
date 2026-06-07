package lk.ashan.routenetlkserverapllication.module.vehicleservice.mapper;

import lk.ashan.routenetlkserverapllication.module.sparepart.mapper.PartMapper;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServicePart;
import org.mapstruct.*;

import java.util.List;

@Mapper(
 componentModel = MappingConstants.ComponentModel.SPRING,
 unmappedTargetPolicy = ReportingPolicy.IGNORE,
 uses = {VehicleServiceMapper.class, PartMapper.class}
)
public interface VehicleServicePartMapper {

    VehicleServicePart toEntity(VehicleServicePartDto dto);

    VehicleServicePartDetailResponseDto toDto(VehicleServicePart entity);

    List<VehicleServicePartDetailResponseDto> toDtoList(List<VehicleServicePart> entities);

}
