package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the state of an incident when it is reported.
 * This state allows transitions to specific other states.
 */
@Component
public class IncidentReportedState implements IncidentState {

    private static final List<String> ALLOWED = List.of("IN PROGRESS");

    /**
     * Transitions the incident to a new status if the transition is valid.
     *
     * @param incident   The incident to transition.
     * @param newStatus  The new status to transition to.
     * @throws InvalidStateTransitionException if the transition is not allowed.
     */
    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("REPORTED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from REPORTED to " + newStatusName
            );
        }
        incident.setIncidentstatus(newStatus);
    }

    /**
     * Validates the initial state of the incident.
     * This method is a no-op for the reported state.
     */
    @Override
    public void validateInitial() { }
}
