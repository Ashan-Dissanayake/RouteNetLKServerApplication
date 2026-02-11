package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

public interface TripState {
    void transitionTo(Trip trip, Tripstatus newStatus);

    default void validateInitial() {
        throw new InvalidStatusTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
