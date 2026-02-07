package lk.ashan.routenetlkserverapllication.module.permit.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleSummaryResponseDto;
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
    private BranchSummaryResponseDto branch;
    private PermitStatusDto permitestatus;
    private ServiceTypeDto servicetype;
    private RouteDto route;

}
