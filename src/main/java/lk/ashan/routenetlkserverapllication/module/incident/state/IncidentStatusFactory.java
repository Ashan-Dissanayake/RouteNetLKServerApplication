package lk.ashan.routenetlkserverapllication.module.incident.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class IncidentStatusFactory {

    private final Map<String, Supplier<IncidentState>> stateMap;

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

    public IncidentState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<IncidentState> supplier = stateMap.get(normalized);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown incident status: " + statusName);
        }
        return supplier.get();
    }
}
