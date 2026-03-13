package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class VehicleServiceSummaryResponseDto {
    private Integer id;
    private VehicleSummaryResponseDto vehicle;
    private BranchSummaryResponseDto branch;
}
