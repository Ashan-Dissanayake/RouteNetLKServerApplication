package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RosterDto {
    private Integer id;
    private LocalDate doroster;
    private ShiftDto shift;
    private BranchSummaryResponseDto branch;
}
