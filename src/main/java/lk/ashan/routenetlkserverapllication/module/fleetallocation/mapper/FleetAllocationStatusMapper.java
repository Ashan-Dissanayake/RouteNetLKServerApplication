package lk.ashan.routenetlkserverapllication.module.fleetallocation.mapper;

import lk.ashan.routenetlkserverapllication.module.fleetallocation.dto.FleetAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.fleetallocation.model.Fleetallocationstatus;

public interface FleetAllocationStatusMapper {

    FleetAllocationStatusDto toDto(Fleetallocationstatus fleetallocationstatus);

}
