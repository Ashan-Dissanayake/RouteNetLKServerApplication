package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.state.SuspendedState;
import lk.ashan.routenetlkserverapllication.module.permit.state.*;
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
                "PLANNED", PlannedState::new,
                "NEEDVEHICLEOVERRIDE", NeedVehicleOverrideState::new,
                "READY", ReadyState::new,
                "INPROGRESS", InProgressState::new,
                "DELAYED", DelayedState::new,
                "SUSPENDED", SuspendedState::new,
                "COMPLETED", CompletedState::new,
                "CANCELLED", CancelledState::new
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
     *
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

    /**
     * Checks if a status name is valid (can be mapped to a state)
     */
    public boolean isValidStatus(String statusName) {
        try {
            String normalized = normalizeStatusName(statusName);
            return stateMap.containsKey(normalized);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the canonical (normalized) status name
     * Useful for logging or display purposes
     */
    public String getCanonicalStatusName(String statusName) {
        String normalized = normalizeStatusName(statusName);

        // Map back to readable format
        return switch (normalized) {
            case "PLANNED" -> "PLANNED";
            case "NEEDVEHICLEOVERRIDE" -> "NEEDS VEHICLE OVERRIDE";
            case "READY" -> "READY";
            case "INPROGRESS" -> "IN_PROGRESS";
            case "DELAYED" -> "DELAYED";
            case "SUSPENDED" -> "SUSPENDED";
            case "COMPLETED" -> "COMPLETED";
            case "CANCELLED" -> "CANCELLED";
            default -> statusName;
        };
    }
}
