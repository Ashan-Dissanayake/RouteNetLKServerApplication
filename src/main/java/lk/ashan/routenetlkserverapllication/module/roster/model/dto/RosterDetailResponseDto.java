package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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
    private BranchSummaryDto branch;
    private LocalDate dostartofweek;
    private LocalDate doendofweek;
    private RosterStatusDto rosterstatus;
    private Collection<ShiftRosterAssignmentDto> shiftrosterassignments;
}
