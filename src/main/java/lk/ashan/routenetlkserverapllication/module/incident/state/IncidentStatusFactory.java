package lk.ashan.routenetlkserverapllication.module.incident.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory class for creating instances of {@link IncidentState} based on the incident status name.
 */
@Component
public class IncidentStatusFactory {

    private final Map<String, Supplier<IncidentState>> stateMap;

    /**
     * Constructs an {@code IncidentStatusFactory} and initializes the mapping of status names
     * to their corresponding {@link IncidentState} suppliers.
     */
    public IncidentStatusFactory() {
        stateMap = Map.of(
                "REPORTED", IncidentReportedState::new,
                "IN PROGRESS", IncidentInProgressState::new,
                "VEHICLE RECOVERY", IncidentVehicleRecoveryState::new,
                "PENDING ALLOCATION", IncidentPendingAllocationState::new,
                "RESOLVED", IncidentResolvedState::new,
                "CLOSED", IncidentClosedState::new
        );
    }

    /**
     * Retrieves the {@link IncidentState} instance corresponding to the given status name.
     *
     * @param statusName the name of the incident status (case-insensitive and trimmed)
     * @return the {@link IncidentState} instance associated with the given status name
     * @throws IllegalArgumentException if the status name is not recognized
     */
    public IncidentState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<IncidentState> supplier = stateMap.get(normalized);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown incident status: " + statusName);
        }
        return supplier.get();
    }
}
