package lk.ashan.routenetlkserverapllication.module.permit.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PermitDetailResponseDto {
    private Integer id;
    private String number;
    private VehicleSummaryResponseDto vehicle;
    private LocalDate doissued;
    private LocalDate doexpired;
    private BranchSummaryDto branch;
    private PermitStatusDto permitestatus;
    private ServiceTypeDto servicetype;
    private RouteSummaryResponseDto route;

}
