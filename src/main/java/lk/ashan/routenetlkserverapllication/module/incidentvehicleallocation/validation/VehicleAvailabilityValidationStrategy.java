package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VehicleAvailabilityValidationStrategy implements AllocationValidationStrategy {

    private final IncidentVehicleAllocationRepository allocationRepository;

    @Override
    public void validate(AllocationContext context) {

        Vehicle vehicle = context.getVehicle();

        if (!vehicle.getVehiclestatus().getName().equalsIgnoreCase("ACTIVE")) {
            throw new BusinessRuleViolationException("Vehicle is not ACTIVE");
        }

        boolean alreadyAllocated =
                allocationRepository.existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                        vehicle.getId(),
                        List.of("Assigned", "In progress")
                );

        if (alreadyAllocated) {
            throw new BusinessRuleViolationException("Vehicle already allocated to active incident");
        }
    }
}
