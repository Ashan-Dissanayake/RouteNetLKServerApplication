package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;

public interface RosterShiftAssignmentState {
    void transitionTo(RosterShiftAssignment assignment, RosterShiftAssignmentStatus newStatus);
}
