package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCancelledState implements TripState {

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CANCELLED"
        );
    }
}
