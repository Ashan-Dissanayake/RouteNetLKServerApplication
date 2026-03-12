package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class ShiftRosterAssignmentStatusFactory {

    private final Map<String, Supplier<ShiftRosterAssignmentState>> stateMap;

    public ShiftRosterAssignmentStatusFactory() {
        stateMap = Map.of(
                "SUGGESTED", RosterAssignmentSuggestedState::new,
                "CONFIRMED", RosterAssignmentConfirmedState::new,
                "REJECTED",  RosterAssignmentRejectedState::new
        );
    }

    public ShiftRosterAssignmentState getState(String statusName) {
        Supplier<ShiftRosterAssignmentState> supplier =
                stateMap.get(statusName.trim().toUpperCase());

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown assignment status: " + statusName
            );
        }
        return supplier.get();
    }
}
