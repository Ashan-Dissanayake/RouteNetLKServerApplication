package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the state of an incident when it is in progress.
 * This state allows transitions to specific statuses only.
 */
@Component
public class IncidentInProgressState implements IncidentState {

    private static final List<String> ALLOWED = List.of("VEHICLE RECOVERY", "PENDING ALLOCATION", "RESOLVED");

    /**
     * Transitions the incident to a new status if the transition is valid.
     *
     * @param incident   The incident whose state is being transitioned.
     * @param newStatus  The new status to transition to.
     * @throws InvalidStateTransitionException if the transition is not allowed.
     */
    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("IN PROGRESS".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from IN_PROGRESS to " + newStatusName
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
