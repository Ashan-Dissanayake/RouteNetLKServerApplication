package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncidentStateValidationStrategy implements AllocationValidationStrategy {

    private final IncidentRepository incidentRepository;

    @Override
    public void validate(AllocationContext context) {
        Incident incident = incidentRepository.findById(context.getIncidentId())
                .orElseThrow(() -> new BusinessRuleViolationException("Incident not found"));

        String status = incident.getIncidentstatus().getName();

        List<String> terminalStatuses = List.of("Closed", "Resolved");

        if (terminalStatuses.stream().anyMatch(s -> s.equalsIgnoreCase(status))) {
            throw new BusinessRuleViolationException(
                    "Cannot allocate vehicle. The Incident is already " + status
            );
        }
    }
}
