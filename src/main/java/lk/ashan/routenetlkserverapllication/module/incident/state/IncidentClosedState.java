package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class IncidentClosedState implements IncidentState {

    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from CLOSED state"
        );
    }

}
