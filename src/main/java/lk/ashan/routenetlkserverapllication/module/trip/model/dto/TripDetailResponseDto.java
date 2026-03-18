package lk.ashan.routenetlkserverapllication.module.trip.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripDetailResponseDto {
    private Integer id;
    private BranchSummaryDto branch;
    private TripTypeDto triptype;
    private PermitSummaryResponseDto permite;
    private LocalDate doservice;
    private LocalTime todepature;
    private LocalTime toarrival;
    private String remarks;
    private Integer notrip;
    private OriginTerminalDto originterminal;
    private TripStatusDto tripstatus;
}
