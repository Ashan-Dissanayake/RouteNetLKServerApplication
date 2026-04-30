package lk.ashan.routenetlkserverapllication.module.roster.model.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RosterRequestDto {
    @NotNull(message = "Branch is mandatory")
    private BranchSummaryDto branch;
    @NotNull(message = "Start date is mandatory")
    @Future(message = "Start date must be in the future")
    private LocalDate dostartofweek;
    @NotNull(message = "End date is mandatory")
    private LocalDate doendofweek;
}

