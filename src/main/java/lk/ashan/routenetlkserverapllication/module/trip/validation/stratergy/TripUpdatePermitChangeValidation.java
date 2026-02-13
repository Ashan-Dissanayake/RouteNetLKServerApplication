package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import org.springframework.stereotype.Component;

/**
 * Validates permit change restrictions during update
 * ISSUE #5: Consistent validation for updates
 */
@Component
public class TripUpdatePermitChangeValidation implements TripUpdateValidationStrategy {
    
    @Override
    public void validate(TripUpdateContext context) {
        
        // If permit hasn't changed, no validation needed
        if (!context.isPermitChanged()) {
            return;
        }
        
        // Check trip status - READY trips cannot change permit
        String currentStatus = context.getExistingTrip().getTripstatus().getName();
        
        if ("READY".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Permit cannot be changed once trip is in READY status"
            );
        }
        
        // Permit can be changed for PLANNED and NEEDS_VEHICLE_OVERRIDE trips
        if (!"PLANNED".equalsIgnoreCase(currentStatus) && 
            !"NEEDS VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus) &&
            !"NEED VEHICLE OVERRIDE".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Permit can only be changed for PLANNED or NEEDS_VEHICLE_OVERRIDE trips. " +
                "Current status: " + currentStatus
            );
        }
        
        // Validate new permit exists and is valid (reuses permit validation logic)
        if (context.getNewPermit() == null) {
            throw new IllegalStateException("New permit not found");
        }

        /*
        // Ensure route consistency if needed
        Integer existingRouteId = context.getExistingTrip().getPermite().getRoute().getId();
        Integer newRouteId = context.getNewPermit().getRoute().getId();

        // Optional: Enforce route consistency (can be relaxed based on business rules)
        if (!existingRouteId.equals(newRouteId)) {
            // Warning: Changing route may affect terminal and time validations
            // This could be logged or validated separately
        }
        */

    }
}
