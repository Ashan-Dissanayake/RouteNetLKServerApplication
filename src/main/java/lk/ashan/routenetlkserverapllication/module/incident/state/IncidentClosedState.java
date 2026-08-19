package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

/**
 * Represents the CLOSED state of an Incident.
 * No transitions are allowed from this state.
 */
@Component
public class IncidentClosedState implements IncidentState {

    /**
     * Handles the transition of an Incident to a new status.
     * In the CLOSED state, no transitions are allowed, and an exception is thrown.
     *
     * @param incident the Incident object to transition
     * @param newStatus the new status to transition to
     * @throws InvalidStateTransitionException if a transition is attempted from the CLOSED state
     */
    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CLOSED state"
        );
    }

}
