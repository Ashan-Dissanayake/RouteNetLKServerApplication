package lk.ashan.routenetlkserverapllication.module.grn.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestSummaryResponseDto;
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
    private BranchSummaryDto branch;
    private PartRequestSummaryResponseDto part;
    private String number;
    private LocalDate doreceived;
    private String remarks;
    private GrnStatusDto grnstatus;
    private List<GrnPartRequestItemDto> grnpartrequestitems;
}
