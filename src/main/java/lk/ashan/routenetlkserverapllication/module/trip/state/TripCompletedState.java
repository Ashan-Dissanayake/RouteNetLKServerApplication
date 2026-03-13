package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class TripCompletedState implements TripState {

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from COMPLETED"
        );
    }
}
