package lk.ashan.routenetlkserverapllication.module.roster.validation.context;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class RosterCreationContext {
    private final Integer currentRosterId;
    private final Integer branchId;
    private final LocalDate dostartofweek;
    private final LocalDate doendofweek;

}
