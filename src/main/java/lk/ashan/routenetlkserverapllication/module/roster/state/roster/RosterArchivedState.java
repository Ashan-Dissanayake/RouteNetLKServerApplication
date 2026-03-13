package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Rosterstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class RosterArchivedState implements RosterState {

    @Override
    public void transitionTo(Roster roster, Rosterstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from ARCHIVED"
        );
    }
}
