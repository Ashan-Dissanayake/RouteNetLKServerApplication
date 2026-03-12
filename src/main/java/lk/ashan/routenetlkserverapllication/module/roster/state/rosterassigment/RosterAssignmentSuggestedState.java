package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class RosterAssignmentSuggestedState implements ShiftRosterAssignmentState {

    private static final List<String> ALLOWED =
            List.of("CONFIRMED", "REJECTED");

    @Override
    public void transitionTo(Shiftrosterassignment assignment, Shiftrosterassignmentstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("SUGGESTED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from SUGGESTED to " + newStatusName
            );
        }

        assignment.setShiftrosterassignmentstatus(newStatus);
    }

    @Override
    public void validateInitial() {
        // SUGGESTED is the only valid initial state for Assignment
    }
}
