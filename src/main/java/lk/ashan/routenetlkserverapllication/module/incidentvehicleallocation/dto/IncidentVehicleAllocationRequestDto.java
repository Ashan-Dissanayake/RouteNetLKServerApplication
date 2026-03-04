package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
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
    @NotNull(message = "Incident is required")
    private IncidentSummaryResponseDto incident;
    @NotNull(message = "Vehicle is required")
    private VehicleSummaryResponseDto vehicle;
    @NotNull(message = "Branch is required")
    private BranchSummaryResponseDto providebranch;
    @NotNull(message = "Type is required")
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    @NotNull(message = "Status is required")
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    @NotNull(message = "Assigned date is required")
    private LocalDateTime doassigned;
    @NotNull(message = "Released date is required")
    private LocalDateTime doreleased;
}
