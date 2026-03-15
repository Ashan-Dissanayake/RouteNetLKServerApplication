package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class RosterWeekValidationStrategy implements RosterCreationStrategy{
    @Override
    public void validate(RosterCreationContext context) {

        LocalDate startDate = context.getDostartofweek();
        LocalDate endDate = context.getDoendofweek();

        // 1. Start must be before end
        if (!startDate.isBefore(endDate)) {
            throw new BusinessRuleViolationException(
                    "Week start date must be before end date"
            );
        }

        // 2. Exactly 7 days (one week)
        long days = ChronoUnit.DAYS.between(startDate, endDate);
        if (days != 6) { // 6 days difference = 7 days total (inclusive)
            throw new BusinessRuleViolationException(
                    "Roster must cover exactly one week (7 days). " +
                            "Current range: " + (days + 1) + " days"
            );
        }

        if (startDate.getDayOfWeek() != DayOfWeek.MONDAY) {
            throw new BusinessRuleViolationException(
                    "Roster week must start on Monday. " +
                            "Given date is: " + startDate.getDayOfWeek()
            );
        }

        // 4. End date must be Sunday
        if (endDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
            throw new BusinessRuleViolationException(
                    "Roster week must end on Sunday. " +
                            "Given date is: " + endDate.getDayOfWeek()
            );
        }

    }
}
