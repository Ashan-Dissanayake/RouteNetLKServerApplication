package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripDraftState implements TripState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "CANCELLED");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("DRAFT".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from DRAFT to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }

    @Override
    public void validateInitial() { }
}

