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
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class IncidentVehicleAllocationCreateRequestDto extends IncidentVehicleAllocationRequestDto{

}
