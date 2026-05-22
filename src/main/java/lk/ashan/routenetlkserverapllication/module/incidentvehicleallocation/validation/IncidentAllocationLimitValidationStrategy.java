package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

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
        boolean hasActive = allocationRepository.existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                context.getIncidentId(),
                List.of("Assigned", "In Progress")
        );
        if (hasActive) {
            throw new BusinessRuleViolationException("This incident already has an active relief bus assigned.");
        }
    }
}
