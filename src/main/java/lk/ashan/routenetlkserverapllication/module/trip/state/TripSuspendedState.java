package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripSuspendedState implements TripState {

    private static final List<String> ALLOWED =
            List.of("IN_PROGRESS", "CANCELLED");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("SUSPENDED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from SUSPENDED to " + newStatusName
            );
        }

        trip.setTripstatus(newStatus);
    }
}
