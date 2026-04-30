package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class RosterShiftAssignmentProposedState implements RosterShiftAssignmentState{
    private static final List<String> ALLOWED = List.of("CONFIRMED", "CANCELED");
    @Override
    public void transitionTo(RosterShiftAssignment assignment, RosterShiftAssignmentStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException("Invalid transition from PROPOSED to " + newStatus.getName());
        }
        assignment.setRostershiftassignmentstatus(newStatus);
    }
}
