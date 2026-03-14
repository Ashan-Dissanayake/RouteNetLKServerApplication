package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class IncidentVehiclellocationReleasedState implements IncidentVehicleAllocationState {

    @Override
    public void transitionTo(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus newStatus) {
        throw new InvalidStateTransitionException(
                "Cannot transition from RELEASED state"
        );
    }
}
