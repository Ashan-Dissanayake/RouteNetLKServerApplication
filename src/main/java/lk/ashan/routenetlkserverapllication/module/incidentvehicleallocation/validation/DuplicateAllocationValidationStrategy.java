package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DuplicateAllocationValidationStrategy implements AllocationValidationStrategy {

    private final IncidentVehicleAllocationRepository allocationRepository;

    @Override
    public void validate(AllocationContext context) {

        boolean exists =
                allocationRepository.existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                        context.getIncidentId(),
                        context.getVehicleId(),
                        List.of("Assigned", "In progress")
                );

        if (exists) {
            throw new BusinessRuleViolationException("Duplicate active allocation for this incident and vehicle");
        }
    }
}
