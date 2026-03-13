package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripExecutionStrategy {
    
    private final TripStatusRepository tripStatusRepository;
    private final TripVehicleOverrideRepository tripVehicleOverrideRepository;

    /**
     * Validates that trip can be executed and transitions it to IN_PROGRESS
     */
    public void executeTrip(Trip trip) {
        
        // Validate current status
        String currentStatus = trip.getTripstatus().getName();
        if (!"READY".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Trip must be in READY status to execute. Current status: " + currentStatus
            );
        }
        
        // Determine effective vehicle
        Vehicle effectiveVehicle = getEffectiveVehicle(trip);
        
        if (effectiveVehicle == null) {
            throw new IllegalStateException(
                "No effective vehicle assigned to trip. Cannot execute."
            );
        }
        
        // Check vehicle availability
        String vehicleStatus = effectiveVehicle.getVehiclestatus().getName();
        if (!"AVAILABLE".equalsIgnoreCase(vehicleStatus) && 
            !"IN OPERATION".equalsIgnoreCase(vehicleStatus)) {
            throw new IllegalStateException(
                "Effective vehicle is not available. Status: " + vehicleStatus
            );
        }
        
        // Transition to IN_PROGRESS
        Tripstatus inProgressStatus = tripStatusRepository.findByName("In progress")
            .orElseThrow(() -> new ResourceNotFoundException("IN PROGRESS status not found"));
        
        trip.setTripstatus(inProgressStatus);
    }
    
    /**
     * Gets the effective vehicle for a trip
     * Priority: Override vehicle > Permit vehicle
     */
    public Vehicle getEffectiveVehicle(Trip trip) {
        // Check for active override
        List<Tripvehicleoverride> overrides = trip.getTripvehicleoverrides().stream()
            .filter(override -> "ACTIVE".equalsIgnoreCase(override.getOverridestatus().getName()))
            .toList();
        
        if (!overrides.isEmpty()) {
            // Use the most recent override
            return overrides.get(overrides.size() - 1).getVehicle();
        }
        
        // Use permit vehicle
        return trip.getPermite().getVehicle();
    }

}
