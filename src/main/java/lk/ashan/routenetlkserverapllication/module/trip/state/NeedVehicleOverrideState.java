package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class NeedVehicleOverrideState implements TripState {

    private static final List<String> ALLOWED = List.of("READY");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("NEEDS VEHICLE OVERRIDE".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from NEEDS VEHICLE OVERRIDE to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }

}

