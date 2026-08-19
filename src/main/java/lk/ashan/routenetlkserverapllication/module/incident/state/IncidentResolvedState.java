package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the state of an incident when it is resolved.
 * This state allows transitions to specific statuses only.
 */
@Component
public class IncidentResolvedState implements IncidentState {

    private static final List<String> ALLOWED = List.of("CLOSED");

    /**
     * Handles the transition of an incident from the RESOLVED state to a new status.
     *
     * @param incident   The incident whose state is being transitioned.
     * @param newStatus  The new status to transition to.
     * @throws InvalidStateTransitionException if the transition to the new status is not allowed.
     */
    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("RESOLVED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from RESOLVED to " + newStatusName
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
