package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incidentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentInProgressState implements IncidentState {

    private static final List<String> ALLOWED = List.of("RESOLVED", "CLOSED");

    @Override
    public void transitionTo(Incident incident, Incidentstatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from IN_PROGRESS to " + newStatus
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
