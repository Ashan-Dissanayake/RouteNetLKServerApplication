package lk.ashan.routenetlkserverapllication.module.incident.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class IncidentStatusFactory {

    private final Map<String, Supplier<IncidentState>> stateMap;

    public IncidentStatusFactory() {
        stateMap = Map.of(
                "REPORTED", ReportedState::new,
                "IN_PROGRESS", InProgressIncidentState::new,
                "RESOLVED", ResolvedState::new,
                "CLOSED", ClosedState::new
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
