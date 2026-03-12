package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class IncidentClosedState implements IncidentState {

    @Override
    public void transitionTo(Incident incident, Incidentstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CLOSED state"
        );
    }

    @Override
    public void validateInitial() {
        // CLOSED can never be initial state
        throw new InvalidStateTransitionException(
                "CLOSED cannot be an initial incident state"
        );
    }
}
