package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripallocationstatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCrewAllocationConfirmedState implements TripCrewAllocationState {

    @Override
    public void transitionTo(Tripcrewallocation allocation, Tripallocationstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CONFIRMED status"
        );
    }
}
