package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.entity.TripCrewAllocation;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripCrewAllocationAllocatedState implements TripCrewAllocationState {

    private static final List<String> ALLOWED = List.of("CONFIRMED", "REJECTED");

    @Override
    public void transitionTo(TripCrewAllocation allocation, TripCrewAllocationStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("ALLOCATED".equals(newStatusName)) return; // Same status, no change

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Cannot transition from ALLOCATED to " + newStatusName +
                            ". Allowed transitions: CONFIRMED, REJECTED"
            );
        }

        allocation.setTripcrewallocationstatus(newStatus);
    }
}
