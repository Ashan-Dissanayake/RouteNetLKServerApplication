package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

/**
 * Represents the state of a trip when it has been discontinued.
 * No transitions are allowed from this state.
 */
@Component
public class TripDiscontinuedState implements TripState {

    /**
     * Throws an exception as no transitions are allowed from the DISCONTINUED state.
     *
     * @param trip the trip entity for which the state transition is attempted
     * @param newStatus the new status to which the transition is attempted
     * @throws InvalidStateTransitionException always thrown to indicate that transitions are not allowed
     */
    @Override
    public void transitionTo(Trip trip, Tripstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from DISCONTINUED"
        );
    }
}
