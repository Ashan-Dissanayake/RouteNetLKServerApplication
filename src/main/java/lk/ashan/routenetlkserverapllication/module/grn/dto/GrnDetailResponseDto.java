package lk.ashan.routenetlkserverapllication.module.grn.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.dto.PartRequestSummaryDetailResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GrnDetailResponseDto {
    private Integer id;
    private BranchSummaryResponseDto branch;
    private PartRequestSummaryDetailResponseDto part;
    private String number;
    private LocalDate doreceived;
    private String remarks;
    private GrnStatusDto grnstatus;
    private List<GrnPartDto> grnparts;
}
