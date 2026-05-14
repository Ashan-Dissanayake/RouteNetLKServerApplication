package lk.ashan.routenetlkserverapllication.module.incident.state;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentVehicleRecoveryState  implements IncidentState{

    private static final List<String> ALLOWED = List.of("PENDING ALLOCATION","RESOLVED");

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
