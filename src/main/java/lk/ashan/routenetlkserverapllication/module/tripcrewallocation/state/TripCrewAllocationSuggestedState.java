package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripallocationstatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.Tripcrewallocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripCrewAllocationSuggestedState implements TripCrewAllocationState {

    private static final List<String> ALLOWED = List.of("CONFIRMED", "REJECTED");

    @Override
    public void transitionTo(Tripcrewallocation allocation, Tripallocationstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("SUGGESTED".equals(newStatusName)) return; // Same status, no change

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Cannot transition from SUGGESTED to " + newStatusName +
                            ". Allowed transitions: CONFIRMED, REJECTED"
            );
        }

        allocation.setTripallocationstatus(newStatus);
    }
}
