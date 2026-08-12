package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncidentVehicleAllocationCreateRequestDto{
    @NotNull(message = "Incident is mandatory")
    private IncidentSummaryDto incident;
    @NotNull(message = "Vehicle is mandatory")
    private VehicleSummaryDto vehicle;
    @NotNull(message = "Provided Branch is mandatory")
    private BranchSummaryDto providedbranch;
    @NotNull(message = "Status is mandatory")
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;

}
