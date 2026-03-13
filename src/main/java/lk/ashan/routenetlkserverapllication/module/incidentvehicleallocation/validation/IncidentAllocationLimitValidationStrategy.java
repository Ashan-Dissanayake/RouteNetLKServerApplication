package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncidentAllocationLimitValidationStrategy implements AllocationValidationStrategy {

    private final IncidentVehicleAllocationRepository allocationRepository;

    @Override
    public void validate(AllocationContext context) {

        Incident incident = context.getIncident();
        String type = incident.getIncidenttype().getName();

        if (type.equalsIgnoreCase("BREAKDOWN")) {

            long activeCount =
                    allocationRepository.countByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                            incident.getId(),
                            List.of("Assigned", "In progress")
                    );

            if (activeCount >= 1) {
                throw new BusinessRuleViolationException("Breakdown allows only one active allocation");
            }
        }
    }
}
