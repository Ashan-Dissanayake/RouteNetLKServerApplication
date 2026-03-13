package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCrewAllocationRejectedState implements TripCrewAllocationState {

    @Override
    public void transitionTo(Tripcrewallocation allocation, Tripallocationstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from REJECTED status. Use delete/clear operation instead."
        );
    }
}
