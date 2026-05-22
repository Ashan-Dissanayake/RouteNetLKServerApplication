package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VehicleAvailabilityValidationStrategy implements AllocationValidationStrategy {

    private final IncidentVehicleAllocationRepository allocationRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public void validate(AllocationContext context) {
        Vehicle vehicle = vehicleRepository.findById(context.getVehicleId()).orElseThrow();

        if (!vehicle.getVehiclestatus().getName().equalsIgnoreCase("Available")) {
            throw new BusinessRuleViolationException("Vehicle is " + vehicle.getVehiclestatus().getName() + " and cannot be used for relief.");
        }

        boolean alreadyAllocated = allocationRepository.existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                context.getVehicleId(),
                List.of("Assigned", "In Progress")
        );

        if (alreadyAllocated) {
            throw new BusinessRuleViolationException("This bus is already assigned to another emergency.");
        }
    }
}
