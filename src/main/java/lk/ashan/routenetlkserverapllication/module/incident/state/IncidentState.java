package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

/**
 * Represents the state of an Incident and provides methods for state transitions.
 */
public interface IncidentState {

    /**
     * Transitions the given Incident to a new status.
     *
     * @param incident the Incident to transition
     * @param newStatus the new status to transition the Incident to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    void transitionTo(Incident incident, IncidentStatus newStatus);

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
