package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentReportedState implements IncidentState {

    private static final List<String> ALLOWED = List.of("IN PROGRESS");

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


    @Override
    public void validateInitial() { }
}
