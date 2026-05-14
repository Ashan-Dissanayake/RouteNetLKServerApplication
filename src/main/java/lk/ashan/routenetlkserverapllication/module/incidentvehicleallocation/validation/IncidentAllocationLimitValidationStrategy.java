package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncidentAllocationLimitValidationStrategy implements AllocationValidationStrategy {

    private final IncidentVehicleAllocationRepository allocationRepository;
    private final IncidentRepository incidentRepository;

    @Override
    public void validate(AllocationContext context) {
        Incident incident = incidentRepository.findById(context.getIncidentId())
                .orElseThrow(() ->
                        new BusinessRuleViolationException(
                                "Incident not found with id: " + context.getIncidentId())
                );

        String type = incident.getIncidenttype().getName();

        if (type.equalsIgnoreCase("BREAKDOWN")){
            long activeCount =
                    allocationRepository.countByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                            context.getIncidentId(),
                            List.of("Assigned", "In progress")
                    );
            if (activeCount >= 1) {
                throw new BusinessRuleViolationException("Breakdown allows only one active allocation");
            }
        }
    }
}
