package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripStatusRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripCancellationStrategy {
    
    private final TripStatusRepository tripStatusRepository;
    private final TripVehicleOverrideRepository tripVehicleOverrideRepository;
    
    /**
     * Cancels a trip, removing overrides and updating status
     * 
     * @param trip The trip to cancel
     * @param requireAuthorization Whether authorization is required (for IN_PROGRESS trips)
     * @throws IllegalStateException if trip cannot be cancelled
     */
    public void cancelTrip(Trip trip, boolean requireAuthorization) {
        
        // Validate current status
        String currentStatus = trip.getTripstatus().getName();
        
        // Cannot cancel completed trips
        if ("COMPLETED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Cannot cancel a completed trip"
            );
        }
        
        // Cannot cancel already cancelled trips
        if ("CANCELLED".equalsIgnoreCase(currentStatus)) {
            throw new IllegalStateException(
                "Trip is already cancelled"
            );
        }
        
        // IN_PROGRESS trips require authorization
        if ("IN PROGRESS".equalsIgnoreCase(currentStatus) && !requireAuthorization) {
            throw new IllegalStateException(
                "Cancellation of IN_PROGRESS trips requires authorization"
            );
        }
        
        // Remove all active vehicle overrides
        removeActiveOverrides(trip);
        
        // Update status to CANCELLED
        Tripstatus cancelledStatus = tripStatusRepository.findByName("Cancelled")
            .orElseThrow(() -> new ResourceNotFoundException("CANCELLED status not found"));
        
        trip.setTripstatus(cancelledStatus);
    }
    
    /**
     * Cancel trip without requiring authorization (for non-IN_PROGRESS trips)
     */
    public void cancelTrip(Trip trip) {
        cancelTrip(trip, false);
    }
    
    /**
     * Removes all active vehicle overrides for a trip
     */
    private void removeActiveOverrides(Trip trip) {
        List<Tripvehicleoverride> activeOverrides = trip.getTripvehicleoverrides().stream()
            .filter(override -> "ACTIVE".equalsIgnoreCase(override.getOverridestatus().getName()))
            .toList();
        
        if (!activeOverrides.isEmpty()) {
            // Delete active overrides
            tripVehicleOverrideRepository.deleteAll(activeOverrides);
            
            // Update the trip's collection
            trip.getTripvehicleoverrides().removeAll(activeOverrides);
        }
    }
    
    /**
     * Checks if a trip can be cancelled
     */
    public boolean canCancel(Trip trip, boolean hasAuthorization) {
        String currentStatus = trip.getTripstatus().getName();
        
        // Cannot cancel completed or already cancelled
        if ("COMPLETED".equalsIgnoreCase(currentStatus) || 
            "CANCELLED".equalsIgnoreCase(currentStatus)) {
            return false;
        }
        
        // IN_PROGRESS requires authorization
        if ("IN PROGRESS".equalsIgnoreCase(currentStatus)) {
            return hasAuthorization;
        }
        
        return true;
    }
}
