package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;


import lk.ashan.routenetlkserverapllication.module.trip.validation.context.TripUpdateContext;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates vehicle availability when permit or time changes
 * Completes the logic that was commented out in original updateTrip()
 * ISSUE #13: Implements the incomplete vehicle availability checking
 */
@Component
@RequiredArgsConstructor
public class TripUpdateVehicleAvailabilityValidation implements TripUpdateValidationStrategy {
    
    private final VehicleConflictDetectionStrategy conflictDetectionStrategy;
    
    @Override
    public void validate(TripUpdateContext context) {
        
        // Only validate if permit or time changed (affects vehicle assignment)
        if (!context.isPermitChanged() && !context.isTimeChanged()) {
            return;
        }
        
        // Get the vehicle that will be used (from new permit if changed, else existing)
        Vehicle vehicleToValidate = context.getNewPermit() != null
            ? context.getNewPermit().getVehicle()
            : context.getExistingTrip().getPermite().getVehicle();
        
        if (vehicleToValidate == null) {
            throw new IllegalStateException("No vehicle associated with permit");
        }
        
        // Check if vehicle is available for the updated time window
        boolean isAvailable = conflictDetectionStrategy.isVehicleAvailable(
            vehicleToValidate,
            context.getNewServiceDate(),
            context.getNewDeparture(),
            context.getNewArrival(),
            context.getExistingTrip().getId()  // Exclude current trip
        );
        
        if (!isAvailable) {
            // Vehicle not available - this is not necessarily an error
            // The trip might need to move to NEEDS_VEHICLE_OVERRIDE status
            // This is informational for the service layer to decide
            // We'll handle this in the service logic, not throw here
            
            // For now, just validate basic availability
            String vehicleStatus = vehicleToValidate.getVehiclestatus().getName();
            if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus) && 
                !"IN OPERATION".equalsIgnoreCase(vehicleStatus)) {
                throw new IllegalStateException(
                    "Vehicle is not in a usable status: " + vehicleStatus
                );
            }
        }
    }
    
    /**
     * Checks if update will require vehicle override
     * Returns true if vehicle becomes unavailable due to update
     */
    public boolean requiresVehicleOverride(TripUpdateContext context) {
        
        if (!context.isPermitChanged() && !context.isTimeChanged()) {
            return false;
        }
        
        Vehicle vehicleToCheck = context.getNewPermit() != null 
            ? context.getNewPermit().getVehicle()
            : context.getExistingTrip().getPermite().getVehicle();
        
        if (vehicleToCheck == null) {
            return true;
        }
        
        return !conflictDetectionStrategy.isVehicleAvailable(
            vehicleToCheck,
            context.getNewServiceDate(),
            context.getNewDeparture(),
            context.getNewArrival(),
            context.getExistingTrip().getId()
        );
    }
}
