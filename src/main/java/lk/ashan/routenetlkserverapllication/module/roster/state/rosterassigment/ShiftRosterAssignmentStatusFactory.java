package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class ShiftRosterAssignmentStatusFactory {

    private final Map<String, Supplier<ShiftRosterAssignmentState>> stateMap;

    public ShiftRosterAssignmentStatusFactory() {
        stateMap = Map.of(
                "SUGGESTED", SuggestedState::new,
                "CONFIRMED", ConfirmedState::new,
                "REJECTED",  RejectedState::new
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
