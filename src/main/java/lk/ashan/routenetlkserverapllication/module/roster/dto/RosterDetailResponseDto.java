package lk.ashan.routenetlkserverapllication.module.roster.dto;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class RosterDetailResponseDto {
    private Integer id;
    private LocalDate doroster;
    private ShiftDto shift;
    private RosterStatusDto rosterstatus;
    private BranchSummaryResponseDto branch;
    private Collection<RosterAssignmentDto> rosterassignements;
}
