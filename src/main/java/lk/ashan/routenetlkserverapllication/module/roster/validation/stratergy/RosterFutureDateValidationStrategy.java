package lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.roster.validation.context.RosterCreationContext;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RosterFutureDateValidationStrategy implements RosterCreationStrategy {
    @Override
    public void validate(RosterCreationContext context) {
        LocalDate startDate = context.getDostartofweek();
        LocalDate today = LocalDate.now();

        // Cannot create roster for past weeks
        if (startDate.isBefore(today)) {
            throw new BusinessRuleViolationException(
                    "Cannot create roster for past weeks. " +
                            "Week starts: " + startDate
            );
        }

        //Cannot create roster too far in advance (e.g., max 2 weeks)
        LocalDate maxFutureDate = today.plusWeeks(2);
        if (startDate.isAfter(maxFutureDate)) {
            throw new BusinessRuleViolationException(
                    "Cannot create roster more than 1 weeks in advance"
            );
        }
    }
}
