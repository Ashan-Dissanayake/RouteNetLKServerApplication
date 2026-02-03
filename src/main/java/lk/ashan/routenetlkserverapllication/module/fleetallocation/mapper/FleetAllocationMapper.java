package lk.ashan.routenetlkserverapllication.module.fleetallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.fleetallocation.dto.FleetAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.model.Fleetallocation;
import lk.ashan.routenetlkserverapllication.module.vehicle.mapper.VehicleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {
                VehicleMapper.class,
                FleetAllocationStatusMapper.class
        }
)
public interface FleetAllocationMapper {

   @Mapping(
            target = "route",
            expression = "java(fleetallocation.getRoster().getRoute().getNumber())"
    )
    FleetAllocationDetailResponseDto toDetailDto(Fleetallocation fleetallocation);
}
