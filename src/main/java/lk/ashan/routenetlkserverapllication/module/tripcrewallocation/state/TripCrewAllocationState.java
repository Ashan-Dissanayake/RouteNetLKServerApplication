package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface TripCrewAllocationState {
    void transitionTo(TripCrewAllocation allocation, TripCrewAllocationStatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
