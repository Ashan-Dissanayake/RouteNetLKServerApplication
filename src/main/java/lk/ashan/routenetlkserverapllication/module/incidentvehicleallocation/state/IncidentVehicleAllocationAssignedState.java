package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class IncidentVehicleAllocationAssignedState implements IncidentVehicleAllocationState {

    private static final List<String> ALLOWED = List.of("IN PROGRESS", "CANCELLED");

    @Override
    public void transitionTo(IncidentVehicleAllocation incidentvehicleallocation, IncidentVehicleAllocationStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from Assigned to " + newStatus
            );
        }
        incidentvehicleallocation.setIncidentvehicleallocationstatus(newStatus);
    }

    @Override
    public void validateInitial() {}
}
