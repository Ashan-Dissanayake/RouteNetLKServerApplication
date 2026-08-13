package lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleServiceCreateRequestDto {
    @NotNull(message = "Branch is required")
    private BranchSummaryDto branch;
    @NotNull(message = "Vehicle is required")
    private VehicleSummaryDto vehicle;
    @NotNull(message = "Service Type is required")
    private VehicleServiceTypeDto vehicleservicetype;
    private IncidentSummaryDto incident;
    @NotNull(message = "Service Status is required")
    private VehicleServiceStatusDto vehicleservicestatus;
    @NotNull(message = "Service Priority is required")
    private VehicleServicePriorityDto vehicleservicepriority;
    @NotNull(message = "Service Parts are required")
    private List<VehicleServicePartDto> vehicleserviceparts;

}
