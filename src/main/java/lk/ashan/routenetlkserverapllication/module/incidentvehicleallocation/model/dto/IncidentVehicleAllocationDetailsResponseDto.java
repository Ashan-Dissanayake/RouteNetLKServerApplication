package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentVehicleAllocationDetailsResponseDto {
    private Integer id;
    private IncidentSummaryDto incident;
    private VehicleSummaryDto vehicle;
    private BranchSummaryDto providedbranch;
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    private LocalDateTime doassigned;
    private LocalDateTime doreleased;
}
