package lk.ashan.routenetlkserverapllication.module.trip.state;


import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

/**
 * Represents the state of a Trip and provides methods for state transitions.
 */
public interface TripState {

    /**
     * Transitions the given Trip to a new state.
     *
     * @param trip      the Trip entity to transition
     * @param newStatus the new status to transition to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    void transitionTo(Trip trip, Tripstatus newStatus);

    /**
     * Validates if the current state can be used as the initial state.
     *
     * @throws InvalidStateTransitionException if the state is not allowed as the initial state
     */
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
