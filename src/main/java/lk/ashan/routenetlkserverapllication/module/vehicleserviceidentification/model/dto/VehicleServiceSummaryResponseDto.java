package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleServiceSummaryResponseDto {
    private Integer id;
    private VehicleSummaryDto vehicle;
    private BranchSummaryDto branch;
}
