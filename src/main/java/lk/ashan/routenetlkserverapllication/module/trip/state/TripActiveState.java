package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the active state of a trip. This state allows transitions
 * to specific statuses such as SUSPENDED, DISCONTINUED, or DRAFT.
 */
@Component
public class TripActiveState implements TripState {

    private static final List<String> ALLOWED = List.of("SUSPENDED", "DISCONTINUED", "DRAFT");

    /**
     * Transitions the trip to a new status if the transition is valid.
     *
     * @param trip the trip entity whose status is to be updated
     * @param newStatus the new status to transition to
     * @throws InvalidStateTransitionException if the transition to the new status is not allowed
     */
    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();

        if ("ACTIVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from ACTIVE to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }
}
