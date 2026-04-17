package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class RosterShiftAssignmentStatusFactory {
    private final Map<String, Supplier<RosterShiftAssignmentState>> stateMap;

    public RosterShiftAssignmentStatusFactory() {
        stateMap = Map.of(
                "DRAFT", RosterShiftAssignmentDraftState::new,
                "PROPOSED", RosterShiftAssignmentProposedState::new,
                "CONFIRMED", RosterShiftAssignmentConfirmedState::new,
                "IN PROGRESS", RosterShiftAssignmentInProgressState::new,
                "COMPLETED", RosterShiftAssignmentCompletedState::new,
                "CANCELLED", RosterShiftAssignmentCancelledState::new
        );
    }

    public RosterShiftAssignmentState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<RosterShiftAssignmentState> supplier = stateMap.get(normalized);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown roster status: " + statusName);
        }
        return supplier.get();
    }
}
