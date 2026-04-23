package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripSuspendedState implements TripState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "DISCONTINUED");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("SUSPENDED".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Suspended schedules can only be Reactivated or Discontinued."
            );
        }
        trip.setTripstatus(newStatus);
    }

}

