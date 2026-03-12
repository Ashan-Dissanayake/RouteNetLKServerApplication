package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class RosterAssignmentConfirmedState implements ShiftRosterAssignmentState {

    // CONFIRMED is terminal UNLESS roster is unlocked back to DRAFT
    // In that case, RosterStateTransitionHandler resets it to SUGGESTED
    // So ConfirmedState itself does not allow transitions

    @Override
    public void transitionTo(Shiftrosterassignment assignment,
                             Shiftrosterassignmentstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        // Allow reset to SUGGESTED (when roster unlocked to DRAFT)
        if ("SUGGESTED".equals(newStatusName)) {
            assignment.setShiftrosterassignmentstatus(newStatus);
            return;
        }

        throw new InvalidStateTransitionException(
                "Invalid status transition from CONFIRMED to " + newStatusName
        );
    }
}
