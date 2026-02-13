package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripvehicleoverride;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripVehicleOverrideRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Handles state transitions with side effects and lifecycle management
 * Encapsulates all state-specific behavior that should occur during transitions
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TripStateTransitionHandler {
    
    private final TripStatusFactory tripStatusFactory;
    private final TripVehicleOverrideRepository tripVehicleOverrideRepository;
    
    /**
     * Performs state transition with all necessary side effects
     * 
     * @param trip The trip to transition
     * @param newStatus The target status
     */
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        
        String currentStatusName = trip.getTripstatus().getName();
        String newStatusName = newStatus.getName();
        
        log.info("Transitioning trip {} from {} to {}", 
            trip.getId(), currentStatusName, newStatusName);
        
        // Execute exit behavior for current state
        executeOnExit(trip, currentStatusName);
        
        // Validate transition is allowed
        TripState currentState = tripStatusFactory.getState(currentStatusName);
        currentState.transitionTo(trip, newStatus);
        
        // Execute entry behavior for new state
        executeOnEnter(trip, newStatusName);
    }
    
    /**
     * Executes behavior when exiting a state
     */
    private void executeOnExit(Trip trip, String statusName) {
        String normalizedStatus = statusName.trim().toUpperCase();

        switch (normalizedStatus) {
            case "READY" -> onExitReady(trip);
            case "IN PROGRESS" -> onExitInProgress(trip);
            case "NEEDS VEHICLE OVERRIDE", "NEED VEHICLE OVERRIDE" -> onExitNeedsVehicleOverride(trip);
            default -> {
            }
            // No exit behavior needed
        }
    }
    
    /**
     * Executes behavior when entering a state
     */
    private void executeOnEnter(Trip trip, String statusName) {
        String normalizedStatus = statusName.trim().toUpperCase();

        switch (normalizedStatus) {
            case "CANCELLED" -> onEnterCancelled(trip);
            case "COMPLETED" -> onEnterCompleted(trip);
            case "IN PROGRESS" -> onEnterInProgress(trip);
            case "READY" -> onEnterReady(trip);
            default -> {
            }
            // No entry behavior needed
        }
    }
    
    // ==================== EXIT BEHAVIORS ====================
    
    private void onExitReady(Trip trip) {
        // Log state transition audit
        log.debug("Exiting READY state for trip {}", trip.getId());
    }
    
    private void onExitInProgress(Trip trip) {
        // Could record actual duration if needed
        log.debug("Exiting IN_PROGRESS state for trip {}", trip.getId());
    }
    
    private void onExitNeedsVehicleOverride(Trip trip) {
        // Clean up any pending solver suggestions if moving away from override state
        log.debug("Exiting NEEDS_VEHICLE_OVERRIDE state for trip {}", trip.getId());
    }
    
    // ==================== ENTRY BEHAVIORS ====================
    
    private void onEnterCancelled(Trip trip) {
        log.info("Entering CANCELLED state for trip {}", trip.getId());
        
        // Remove all active vehicle overrides
        List<Tripvehicleoverride> activeOverrides = trip.getTripvehicleoverrides().stream()
            .filter(override -> "ACTIVE".equalsIgnoreCase(override.getOverridestatus().getName()))
            .toList();
        
        if (!activeOverrides.isEmpty()) {
            log.debug("Removing {} active override(s) for cancelled trip {}", 
                activeOverrides.size(), trip.getId());
            tripVehicleOverrideRepository.deleteAll(activeOverrides);
            trip.getTripvehicleoverrides().removeAll(activeOverrides);
        }
        
        // Vehicle is implicitly released - no explicit status change needed
        // Future queries will exclude cancelled trips from conflict detection
    }
    
    private void onEnterCompleted(Trip trip) {
        log.info("Entering COMPLETED state for trip {}", trip.getId());
        
        // Vehicle is implicitly released for future scheduling
        // Historical data is preserved
        // No further modifications allowed
        
        // Could trigger completion events/notifications here if needed
    }
    
    private void onEnterInProgress(Trip trip) {
        log.info("Entering IN_PROGRESS state for trip {}", trip.getId());
        
        // Could record actual departure time if needed
        // Could update vehicle status to IN_OPERATION if required
    }
    
    private void onEnterReady(Trip trip) {
        log.info("Entering READY state for trip {}", trip.getId());
        
        // Trip is now ready for execution
        // Ensure effective vehicle is assigned (either permit vehicle or override)
    }
    
    /**
     * Validates if a transition is possible without executing it
     */
    public boolean canTransitionTo(Trip trip, Tripstatus newStatus) {
        try {
            String currentStatusName = trip.getTripstatus().getName();
            TripState currentState = tripStatusFactory.getState(currentStatusName);
            
            // Create a temporary trip clone to test transition
            Trip testTrip = new Trip();
            testTrip.setTripstatus(trip.getTripstatus());
            
            currentState.transitionTo(testTrip, newStatus);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
