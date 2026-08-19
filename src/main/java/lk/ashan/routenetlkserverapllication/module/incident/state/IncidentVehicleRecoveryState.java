package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the state of an incident when it is in the "VEHICLE RECOVERY" state.
 * This state allows transitions to specific other states as defined in the `ALLOWED` list.
 */
@Component
public class IncidentVehicleRecoveryState implements IncidentState {

    private static final List<String> ALLOWED = List.of("PENDING ALLOCATION", "RESOLVED");

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
        if ("VEHICLE RECOVERY".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from VEHICLE RECOVERY to " + newStatusName
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
