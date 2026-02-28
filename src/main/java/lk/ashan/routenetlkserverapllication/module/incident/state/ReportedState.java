package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReportedState implements IncidentState {

    private static final List<String> ALLOWED = List.of("IN_PROGRESS");

    @Override
    public void transitionTo(Incident incident, Incidentstatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from REPORTED to " + newStatus
            );
        }
        incident.setIncidentstatus(newStatus);
    }
}
