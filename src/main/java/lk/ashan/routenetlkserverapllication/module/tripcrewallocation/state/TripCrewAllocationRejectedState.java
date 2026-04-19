package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCrewAllocationRejectedState implements TripCrewAllocationState {

    @Override
    public void transitionTo(TripCrewAllocation allocation, TripCrewAllocationStatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from REJECTED status. Use delete/clear operation instead."
        );
    }
}
