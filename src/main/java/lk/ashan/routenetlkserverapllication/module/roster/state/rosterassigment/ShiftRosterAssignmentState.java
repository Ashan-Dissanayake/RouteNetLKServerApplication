package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface ShiftRosterAssignmentState {
    void transitionTo(Shiftrosterassignment assignment, Shiftrosterassignmentstatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
