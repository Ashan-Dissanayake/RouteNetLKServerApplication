package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class TripStatusValidation implements IncidentCreationStrategy {

    @Override
    public void validate(IncidentContext context) {
        String status = context.getTrip().getTripstatus().getName().toUpperCase();
        if (status.equals("COMPLETED") || status.equals("CANCELLED")) {
            throw new BusinessRuleViolationException("Cannot create incident for completed or cancelled trip");
        }
    }
}
