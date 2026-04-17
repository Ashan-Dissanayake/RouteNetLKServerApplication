package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class RosterShiftAssignmentCancelledState implements RosterShiftAssignmentState{
    @Override
    public void transitionTo(RosterShiftAssignment assignment, RosterShiftAssignmentStatus newStatus) {
        throw new InvalidStateTransitionException(
                "Cancelled shifts are terminal and cannot transition to " + newStatus.getName()
        );
    }
}
