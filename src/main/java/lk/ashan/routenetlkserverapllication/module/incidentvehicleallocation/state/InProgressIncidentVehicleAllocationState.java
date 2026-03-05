package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocationstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InProgressIncidentVehicleAllocationState implements IncidentVehicleAllocationState {

    private static final List<String> ALLOWED = List.of("RELEASED", "CANCELLED");

    @Override
    public void transitionTo(Incidentvehicleallocation allocation, Incidentvehicleallocationstatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from IN_PROGRESS to " + newStatus
            );
        }
        allocation.setIncidentvehicleallocationstatus(newStatus);
        if (newStatus.getName().equals("RELEASED")) {
            allocation.setDoreleased(LocalDateTime.now());
        }
    }
}
