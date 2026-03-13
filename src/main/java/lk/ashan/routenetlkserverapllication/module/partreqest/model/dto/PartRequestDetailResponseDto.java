package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

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
    private LocalDate dorequested;
    private PartRequestStatusDto partrequeststatus;
    private List<PartRequestItemDto> partrequestitems;
}
