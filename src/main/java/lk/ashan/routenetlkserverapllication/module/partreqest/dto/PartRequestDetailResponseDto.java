package lk.ashan.routenetlkserverapllication.module.partreqest.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartRequestDetailResponseDto {
    private Integer id;
    private BranchSummaryResponseDto branch;
    private String number;
    private String remarks;
    private PartRequestStatusDto partrequeststatus;
}
