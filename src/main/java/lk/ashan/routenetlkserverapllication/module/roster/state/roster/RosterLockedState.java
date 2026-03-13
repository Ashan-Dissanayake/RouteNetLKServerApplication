package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Rosterstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class RosterLockedState implements RosterState {

    private static final List<String> ALLOWED =
            List.of("ARCHIVED", "DRAFT"); // Can unlock back to DRAFT

    @Override
    public void transitionTo(Roster roster, Rosterstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("LOCKED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from LOCKED to " + newStatusName
            );
        }

        roster.setRosterstatus(newStatus);
    }
}
