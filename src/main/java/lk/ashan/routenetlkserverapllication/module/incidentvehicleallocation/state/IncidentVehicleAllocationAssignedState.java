package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentVehicleAllocationAssignedState implements IncidentVehicleAllocationState {

    private static final List<String> ALLOWED = List.of("IN PROGRESS", "CANCELLED");

    @Override
    public void transitionTo(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from ASSIGNED to " + newStatus.getName()
            );
        }
        allocation.setIncidentvehicleallocationstatus(newStatus);
    }

    @Override
    public void validateInitial() {}
}
