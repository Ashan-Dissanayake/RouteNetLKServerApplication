package lk.ashan.routenetlkserverapllication.module.roster.state;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class SolveState implements RosterState {
    
    private static final List<String> ALLOWED = List.of("LOCKED");

    @Override
    public void transitionTo(Roster roster, Rosterstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("LOCKED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                "Invalid status transition from Solved to " + newStatusName
            );
        }
    }
}
