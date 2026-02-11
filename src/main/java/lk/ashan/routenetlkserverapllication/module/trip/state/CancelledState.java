package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.module.permit.state.PermitState;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

public class CancelledState implements TripState {

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        throw new InvalidStatusTransitionException(
                "No transitions allowed from CANCELLED"
        );
    }
}
