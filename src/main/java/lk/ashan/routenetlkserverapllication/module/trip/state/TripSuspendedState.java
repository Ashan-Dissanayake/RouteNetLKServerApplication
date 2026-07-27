package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the suspended state of a trip. This state allows transitions
 * to specific states such as ACTIVE or DISCONTINUED, while preventing
 * invalid state transitions.
 */
@Component
public class TripSuspendedState implements TripState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "DISCONTINUED");

    /**
     * Transitions the trip to a new state if the transition is valid.
     *
     * @param trip       The trip entity whose state is being transitioned.
     * @param newStatus  The new status to transition the trip to.
     * @throws InvalidStateTransitionException if the transition is not allowed.
     */
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
