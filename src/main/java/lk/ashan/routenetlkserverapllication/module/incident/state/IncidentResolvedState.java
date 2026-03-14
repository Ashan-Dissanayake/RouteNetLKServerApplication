package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentResolvedState implements IncidentState {

    private static final List<String> ALLOWED = List.of("CLOSED");

    @Override
    public void transitionTo(Incident incident, IncidentStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from RESOLVED to " + newStatus
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
