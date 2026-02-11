package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class PlannedState implements TripState {

    private static final List<String> ALLOWED = List.of("READY", "NEEDS VEHICLE OVERRIDE","CANCELLED");

    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("PLANNED".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from PLANNED to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }

    @Override
    public void validateInitial() { }
}

