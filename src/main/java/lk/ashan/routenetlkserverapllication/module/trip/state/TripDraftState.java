package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the draft state of a Trip. This state allows transitions
 * to specific statuses and validates the transitions accordingly.
 */
@Component
public class TripDraftState implements TripState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "CANCELLED");

    /**
     * Transitions the trip to a new status if the transition is valid.
     *
     * @param trip      The trip entity whose status is to be updated.
     * @param newStatus The new status to transition to.
     * @throws InvalidStateTransitionException if the transition is not allowed.
     */
    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("DRAFT".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from DRAFT to " + newStatusName
            );
        }
        trip.setTripstatus(newStatus);
    }

    /**
     * Validates the initial state of the trip. This method is a no-op
     * for the draft state.
     */
    @Override
    public void validateInitial() { }
}
