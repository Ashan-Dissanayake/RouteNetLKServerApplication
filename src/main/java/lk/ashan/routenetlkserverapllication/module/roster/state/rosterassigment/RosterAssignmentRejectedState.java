package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class RosterAssignmentRejectedState implements ShiftRosterAssignmentState {

    private static final List<String> ALLOWED =
            List.of("SUGGESTED"); // Can re-suggest after rejection

    @Override
    public void transitionTo(Shiftrosterassignment assignment,
                             Shiftrosterassignmentstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("REJECTED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from REJECTED to " + newStatusName
            );
        }

        assignment.setShiftrosterassignmentstatus(newStatus);
    }
}
