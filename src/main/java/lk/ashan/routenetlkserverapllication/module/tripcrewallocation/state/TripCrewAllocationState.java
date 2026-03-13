package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface TripCrewAllocationState {
    void transitionTo(Tripcrewallocation allocation, Tripallocationstatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
