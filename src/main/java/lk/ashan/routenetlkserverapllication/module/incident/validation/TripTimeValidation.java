package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class TripTimeValidation implements IncidentCreationStrategy {

    @Override
    public void validate(IncidentCreationContext context) {
        LocalTime reported = context.getReportedTime();
        if (reported.isBefore(context.getTrip().getTodepature()) ||
                reported.isAfter(context.getTrip().getToarrival())) {
            throw new BusinessRuleViolationException("Incident time must be within trip duration");
        }
    }
}
