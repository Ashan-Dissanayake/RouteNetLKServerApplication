package lk.ashan.routenetlkserverapllication.module.vehicleservice.validation;

import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class VehicleServiceCreationValidationStrategy {

    private final VehicleRepository vehicleRepository;
    private final IncidentRepository incidentRepository;

    public void validate(VehicleServiceValidationContext context) {
        // 1. Business Sanity: Part quantities must be positive
        if (context.getParts() != null) {
            for (VehicleServicePartDto part : context.getParts()) {
                if (part.getQuantity() == null || part.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessRuleViolationException("Requested part quantity must be greater than zero");
                }
            }
        }

        // 2. Conditional Alignment: Breakdown repairs must have an associated incident
        if ("BREAKDOWN_REPAIR".equalsIgnoreCase(context.getServiceTypeName())) {
            if (context.getIncidentId() == null) {
                throw new BusinessRuleViolationException("An active incident ID must be attached for breakdown repairs");
            }
            // Ensure the incident actually exists
            incidentRepository.findById(context.getIncidentId())
                    .orElseThrow(() -> new BusinessRuleViolationException("Attached incident target not found"));
        }

        // 3. Status Guard: Ensure the vehicle is not already locked in another active maintenance ticket
        Vehicle vehicle = vehicleRepository.findById(context.getVehicleId())
                .orElseThrow(() -> new BusinessRuleViolationException("Vehicle target not found"));

        // Adjust condition matching your actual vehicle entity status attribute (e.g., UNDER_MAINTENANCE)
        if ("UNDER_MAINTENANCE".equalsIgnoreCase(vehicle.getVehiclestatus().getName())) {
            throw new BusinessRuleViolationException("This vehicle is already booked into a maintenance loop");
        }
    }
}
