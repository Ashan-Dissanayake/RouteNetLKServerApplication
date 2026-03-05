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
    @NotNull(message = "Incident is mandatory")
    private IncidentSummaryResponseDto incident;
    @NotNull(message = "Vehicle is mandatory")
    private VehicleSummaryResponseDto vehicle;
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryResponseDto providebranch;
    @NotNull(message = "Type is mandatory")
    private IncidentVehicleAllocationTypeDto incidentvehicleallocationtype;
    @NotNull(message = "Status is mandatory")
    private IncidentVehicleAllocationStatusDto incidentvehicleallocationstatus;
    @NotNull(message = "Assigned date is mandatory")
    private LocalDateTime doassigned;
    @NotNull(message = "Released date is mandatory")
    private LocalDateTime doreleased;
}
