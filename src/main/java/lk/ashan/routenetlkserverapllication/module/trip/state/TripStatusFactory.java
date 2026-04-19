package lk.ashan.routenetlkserverapllication.module.trip.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;


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

    private String normalizeStatusName(String statusName) {
        if (statusName == null) {
            throw new IllegalArgumentException("Status name cannot be null");
        }

        String normalized = statusName
                .trim()
                .toUpperCase()
                .replaceAll("[\\s_-]+", "");

        normalized = normalized
                .replace("NEEDS", "NEED")
                .replace("COMPLETE", "COMPLETED");

        return normalized;
    }

}

