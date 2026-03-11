package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
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
