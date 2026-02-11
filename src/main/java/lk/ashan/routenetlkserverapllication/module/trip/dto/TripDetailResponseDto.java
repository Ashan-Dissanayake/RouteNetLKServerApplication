package lk.ashan.routenetlkserverapllication.module.trip.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.permit.dto.PermitSummaryResponseDto;
import lombok.*;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TripDetailResponseDto {
    private BranchSummaryResponseDto branch;
    private TripTypeDto triptype;
    private PermitSummaryResponseDto permite;
    private LocalDate doservice;
    private Time todepature;
    private Time toarrival;
    private String remarks;
    private Integer notrip;
    private OriginTerminalDto originterminal;
    private TripStatusDto tripstatus;
}
