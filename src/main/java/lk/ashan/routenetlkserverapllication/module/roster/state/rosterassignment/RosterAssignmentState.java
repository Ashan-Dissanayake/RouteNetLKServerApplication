package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassignment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignement;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterassignementstatus;

public interface RosterAssignmentState {
    void transitionTo(Rosterassignement rosterAssignement, Rosterassignementstatus newStatus);
}
