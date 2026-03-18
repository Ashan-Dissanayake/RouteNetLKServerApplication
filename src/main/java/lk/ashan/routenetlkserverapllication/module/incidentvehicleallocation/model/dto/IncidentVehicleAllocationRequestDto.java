package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
public class IncidentVehicleAllocationRequestDto {
    @NotNull(message = "Incident is mandatory")
    private IncidentSummaryDto incident;
    @NotNull(message = "Vehicle is mandatory")
    private VehicleSummaryResponseDto vehicle;
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto providebranch;
    @NotNull(message = "Type is mandatory")
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    @NotNull(message = "Status is mandatory")
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    @NotNull(message = "Assigned date is mandatory")
    private LocalDateTime doassigned;
    @NotNull(message = "Released date is mandatory")
    private LocalDateTime doreleased;
}
