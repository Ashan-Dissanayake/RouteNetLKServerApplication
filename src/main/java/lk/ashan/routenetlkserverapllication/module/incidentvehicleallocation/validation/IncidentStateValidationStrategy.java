package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentService;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentStatusService;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IncidentStateValidationStrategy implements AllocationValidationStrategy {

    private final IncidentRepository incidentRepository;

    @Override
    public void validate(AllocationContext context) {
        Incident incident = incidentRepository.findById(context.getIncidentId())
                .orElseThrow(()->new
                        BusinessRuleViolationException(
                                "Incident not found with ID: " + context.getIncidentId())
                );

                String status = incident.getIncidentstatus().getName();

        if (status.equalsIgnoreCase("CLOSED") || status.equalsIgnoreCase("RESOLVED")) {
            throw new BusinessRuleViolationException(
                    "Cannot allocate vehicle for incident in state: " + status
            );
        }
    }
}
