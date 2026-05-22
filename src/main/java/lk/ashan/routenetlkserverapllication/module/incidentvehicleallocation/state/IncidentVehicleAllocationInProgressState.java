package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class IncidentVehicleAllocationInProgressState implements IncidentVehicleAllocationState {

    private static final List<String> ALLOWED = List.of("RELEASED", "CANCELLED");

    @Override
    public void transitionTo(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from IN_PROGRESS to " + newStatus.getName()
            );
        }
        allocation.setIncidentvehicleallocationstatus(newStatus);
    }
}
