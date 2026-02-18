package lk.ashan.routenetlkserverapllication.module.roster.state.roster;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class RosterStatusFactory {

    private final Map<String, Supplier<RosterState>> stateMap;

    public RosterStatusFactory() {
        stateMap = Map.of(
                "DRAFT",    DraftState::new,
                "LOCKED",   LockedState::new,
                "ARCHIVED", ArchivedState::new
        );
    }

    public RosterState getState(String statusName) {
        Supplier<RosterState> supplier =
                stateMap.get(statusName.trim().toUpperCase());

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown roster status: " + statusName
            );
        }
        return supplier.get();
    }
}
