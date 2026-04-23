package lk.ashan.routenetlkserverapllication.module.trip.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;


@Component
public class TripStatusFactory {
    private final Map<String, Supplier<TripState>> stateMap;

    public TripStatusFactory() {
        stateMap = Map.of(
                "DRAFT", TripDraftState::new,
                "ACTIVE", TripActiveState::new,
                "SUSPENDED", TripSuspendedState::new,
                "DISCONTINUED", TripDiscontinuedState::new
        );
    }

    public TripState getState(String statusName) {
        return stateMap.getOrDefault(statusName.trim().toUpperCase(), () -> {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }).get();
    }
}
