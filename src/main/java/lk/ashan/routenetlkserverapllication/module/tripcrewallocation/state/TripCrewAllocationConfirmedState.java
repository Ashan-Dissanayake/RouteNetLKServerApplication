package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCrewAllocationConfirmedState implements TripCrewAllocationState {

    @Override
    public void transitionTo(TripCrewAllocation allocation, TripCrewAllocationStatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CONFIRMED status"
        );
    }
}
