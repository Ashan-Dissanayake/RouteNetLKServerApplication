package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class DraftState implements RosterState {

    private static final List<String> ALLOWED =
            List.of("LOCKED", "ARCHIVED");

    @Override
    public void transitionTo(Roster roster, Rosterstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("DRAFT".equals(newStatusName)) return; // Same state, ignore

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from DRAFT to " + newStatusName
            );
        }

        roster.setRosterstatus(newStatus);
    }

    @Override
    public void validateInitial() {
        //DRAFT is the only valid initial state for Roster
    }
}
