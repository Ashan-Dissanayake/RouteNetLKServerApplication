package lk.ashan.routenetlkserverapllication.module.roster.state;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;

public interface RosterState {
    void transitionTo(Roster roster, Rosterstatus newStatus);
}
