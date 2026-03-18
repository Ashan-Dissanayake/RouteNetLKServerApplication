package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
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
    private BranchSummaryDto branch;
    @NotNull(message = "Date of week start is mandatory")
    @FutureOrPresent(message = "Date of week start must be in the today or future date")
    private LocalDate dostartofweek;
    @NotNull(message = "Date of week end is mandatory")
    private LocalDate doendofweek;
}
