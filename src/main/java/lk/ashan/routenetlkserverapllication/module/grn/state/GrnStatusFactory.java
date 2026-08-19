package lk.ashan.routenetlkserverapllication.module.grn.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory class for creating instances of {@link GrnState} based on the provided status name.
 */
@Component
public class GrnStatusFactory {

    private final Map<String, Supplier<GrnState>> stateMap;

    /**
     * Constructs a new {@code GrnStatusFactory} and initializes the state map
     * with predefined GRN statuses and their corresponding state suppliers.
     */
    public GrnStatusFactory() {
        stateMap = Map.of(
                "DRAFT", GrnDraftState::new,
                "PARTIALLY_RECEIVED", GrnPartiallyReceivedState::new,
                "RECEIVED", GrnReceivedState::new
        );
    }

    /**
     * Retrieves the {@link GrnState} instance corresponding to the given status name.
     *
     * @param statusName the name of the GRN status (case-insensitive)
     * @return the {@link GrnState} instance associated with the given status name
     * @throws IllegalArgumentException if the provided status name is unknown
     */
    public GrnState getState(String statusName) {

        String normalized = statusName.trim().toUpperCase();

        Supplier<GrnState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown GRN status: " + statusName
            );
        }

        return supplier.get();
    }
}
