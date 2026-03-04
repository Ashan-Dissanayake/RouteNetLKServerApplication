package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class IncidentStateValidationStrategy implements AllocationValidationStrategy {

    @Override
    public void validate(AllocationContext context) {

        String status = context.getIncident().getIncidentstatus().getName();

        if (status.equalsIgnoreCase("CLOSED") || status.equalsIgnoreCase("RESOLVED")) {
            throw new BusinessRuleViolationException(
                    "Cannot allocate vehicle for incident in state: " + status
            );
        }
    }
}
