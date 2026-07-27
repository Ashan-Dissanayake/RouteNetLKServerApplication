package lk.ashan.routenetlkserverapllication.module.trip.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;


/**
 * Factory class for creating instances of {@link TripState} based on a given status name.
 * This class uses a map to associate status names with their corresponding state implementations.
 */
@Component
public class TripStatusFactory {
    private final Map<String, Supplier<TripState>> stateMap;

    /**
     * Constructs a new {@code TripStatusFactory} and initializes the state map
     * with predefined status-to-state mappings.
     */
    public TripStatusFactory() {
        stateMap = Map.of(
                "DRAFT", TripDraftState::new,
                "ACTIVE", TripActiveState::new,
                "SUSPENDED", TripSuspendedState::new,
                "DISCONTINUED", TripDiscontinuedState::new
        );
    }

    /**
     * Retrieves the {@link TripState} instance corresponding to the given status name.
     *
     * @param statusName the name of the status for which the state is to be retrieved
     * @return the {@link TripState} instance corresponding to the given status name
     * @throws IllegalArgumentException if the given status name is unknown
     */
    public TripState getState(String statusName) {
        return stateMap.getOrDefault(statusName.trim().toUpperCase(), () -> {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }).get();
    }
}
