package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentVehicleAllocationDetailsResponseDto {
    private Integer id;
    private IncidentSummaryResponseDto name;
    private VehicleSummaryResponseDto vehicle;
    private BranchSummaryResponseDto providebranch;
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    private LocalDateTime doassigned;
    private LocalDateTime doreleased;
}
