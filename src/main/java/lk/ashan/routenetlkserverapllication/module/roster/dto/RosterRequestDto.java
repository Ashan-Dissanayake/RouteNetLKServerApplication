package lk.ashan.routenetlkserverapllication.module.roster.dto;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.shared.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class RosterRequestDto{
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryResponseDto branch;
    @NotNull(message = "Date of week start is mandatory")
    private LocalDate dostartofweek;
    @NotNull(message = "Date of week end is mandatory")
    private LocalDate doendofweek;
}
