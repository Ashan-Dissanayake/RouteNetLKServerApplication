package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryResponseDto;
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
    private IncidentSummaryResponseDto name;
    private VehicleSummaryResponseDto vehicle;
    private BranchSummaryResponseDto providebranch;
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    private LocalDateTime doassigned;
    private LocalDateTime doreleased;
}
