package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class RosterStateFactory {

    private final Map<String, Supplier<RosterState>> stateMap;

    public RosterStateFactory() {
        // Initialize map with suppliers for each state
        stateMap = Map.of(
            "DRAFT", DraftState::new,
            "SOLVED", SolveState::new,
            "LOCKED", LockState::new,
            "REJECTED", RejectState::new
        );
    }

    public RosterState getState(String statusName) {
        Supplier<RosterState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
