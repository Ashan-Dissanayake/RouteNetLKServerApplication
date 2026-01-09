package lk.ashan.routenetlkserverapllication.module.roster.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Collection;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class RosterRequestDto {
    @NotNull(message = "Roster date can not be empty")
    @Future(message = "Roster date can not be past")
    private LocalDate doroster;
    @NotNull(message = "Shift can not be empty")
    private ShiftDto shift;
    @NotNull(message = "Roster Status can not be empty")
    private RosterStatusDto rosterstatus;
    @NotNull(message = "Branch can not be empty")
    private BranchSummaryResponseDto branch;
//    @NotNull(message = "Roster assignments can not be empty")
//    private Collection<RosterAssignmentDto> rosterassignements;
}
