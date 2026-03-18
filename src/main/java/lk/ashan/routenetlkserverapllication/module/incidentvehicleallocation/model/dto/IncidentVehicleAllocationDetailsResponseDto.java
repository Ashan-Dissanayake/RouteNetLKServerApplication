package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentVehicleAllocationDetailsResponseDto {
    private Integer id;
    private IncidentSummaryDto name;
    private VehicleSummaryResponseDto vehicle;
    private BranchSummaryDto providebranch;
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    private LocalDateTime doassigned;
    private LocalDateTime doreleased;
}
