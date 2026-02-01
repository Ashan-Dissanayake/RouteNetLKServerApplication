package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassignment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class CancelState implements RosterAssignmentState {
    
    private static final List<String> ALLOWED = List.of();

    @Override
    public void transitionTo(Rosterassignement rosterassignement, Rosterassignementstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                "Invalid status transition from CONFIRMED to " + newStatusName
            );
        }
    }
}
