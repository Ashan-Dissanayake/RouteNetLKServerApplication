package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassignment;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class RosterAssignmentStateFactory {

    private final Map<String, Supplier<RosterAssignmentState>> stateMap;

    public RosterAssignmentStateFactory() {
        // Initialize map with suppliers for each state
        stateMap = Map.of(
            "PLANNED", PlanState::new,
            "CONFIRMED", ConfirmState::new,
            "CANCELLED", CancelState::new
        );
    }

    public RosterAssignmentState getState(String statusName) {
        Supplier<RosterAssignmentState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
