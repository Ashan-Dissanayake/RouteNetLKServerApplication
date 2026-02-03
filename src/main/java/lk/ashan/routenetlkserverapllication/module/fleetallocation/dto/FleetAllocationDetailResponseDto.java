package lk.ashan.routenetlkserverapllication.module.fleetallocation.dto;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Setter
@Getter
public class FleetAllocationDetailResponseDto {

    private VehicleSummaryResponseDto vehicle;
    private FleetAllocationStatusDto fleetAllocationstatus;
    private Time todepature;
    private Time toreturn;
    private String route;

}
