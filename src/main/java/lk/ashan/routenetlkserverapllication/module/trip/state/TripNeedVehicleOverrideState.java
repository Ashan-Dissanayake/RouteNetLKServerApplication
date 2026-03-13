package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripNeedVehicleOverrideState implements TripState {

    private static final List<String> ALLOWED = List.of("READY");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("NEEDS VEHICLE OVERRIDE".equals(newStatusName) ||
                "NEED VEHICLE OVERRIDE".equals(newStatusName)) {
            return;
        }
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from NEEDS VEHICLE OVERRIDE to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }

    @Override
    public void validateInitial() {
        // NEEDS VEHICLE OVERRIDE is allowed as initial state
        // This happens when vehicle is unavailable during trip creation
    }
}

