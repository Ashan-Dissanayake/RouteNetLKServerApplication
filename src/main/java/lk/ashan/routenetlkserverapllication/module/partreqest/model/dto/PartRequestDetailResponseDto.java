package lk.ashan.routenetlkserverapllication.module.partreqest.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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
    private BranchSummaryDto branch;
    private String number;
    private String remarks;
    private LocalDate dorequested;
    private PartRequestStatusDto partrequeststatus;
    private List<PartRequestItemDto> partrequestitems;
}
