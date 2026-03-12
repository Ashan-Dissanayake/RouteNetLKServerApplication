package lk.ashan.routenetlkserverapllication.module.trip.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * ISSUE #8: Enhanced to handle status name variations
 * Normalizes status names to handle case, whitespace, and format differences
 */
@Component
public class TripStatusFactory {

    private final Map<String, Supplier<TripState>> stateMap;

    public TripStatusFactory() {
        stateMap = Map.of(
                "PLANNED", TripPlannedState::new,
                "NEEDVEHICLEOVERRIDE", TripNeedVehicleOverrideState::new,
                "READY", TripReadyState::new,
                "INPROGRESS", TripInProgressState::new,
                "DELAYED", TripDelayedState::new,
                "SUSPENDED", TripSuspendedState::new,
                "COMPLETED", TripCompletedState::new,
                "CANCELLED", TripCancelledState::new
        );
    }

    public TripState getState(String statusName) {

        // ISSUE #8: Normalize status name to handle variations
        String normalizedStatus = normalizeStatusName(statusName);

        Supplier<TripState> supplier = stateMap.get(normalizedStatus);

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown trip status: '" + statusName + "' " +
                            "(normalized to: '" + normalizedStatus + "')"
            );
        }

        return supplier.get();
    }

    /**
     * Normalizes status name to handle variations:
     * - Converts to uppercase
     * - Removes all whitespace
     * - Handles common variations
     * Examples:
     * "Need vehicle override" → "NEEDVEHICLEOVERRIDE"
     * "NEEDS VEHICLE OVERRIDE" → "NEEDVEHICLEOVERRIDE"
     * "need_vehicle_override" → "NEEDVEHICLEOVERRIDE"
     * "In Progress" → "INPROGRESS"
     * "IN_PROGRESS" → "INPROGRESS"
     */
    private String normalizeStatusName(String statusName) {
        if (statusName == null) {
            throw new IllegalArgumentException("Status name cannot be null");
        }

        // Convert to uppercase and remove all whitespace and underscores
        String normalized = statusName
                .trim()
                .toUpperCase()
                .replaceAll("[\\s_-]+", "");

        // Handle common variations
        normalized = normalized
                .replace("NEEDS", "NEED")  // "NEEDS VEHICLE OVERRIDE" → "NEED VEHICLE OVERRIDE"
                .replace("COMPLETE", "COMPLETED");  // Handle "COMPLETE" vs "COMPLETED"

        return normalized;
    }

}

