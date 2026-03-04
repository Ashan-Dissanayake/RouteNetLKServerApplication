package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocationstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class ReleasedState implements IncidentVehicleAllocationState {

    @Override
    public void transitionTo(Incidentvehicleallocation allocation, Incidentvehicleallocationstatus newStatus) {
        throw new InvalidStateTransitionException(
                "Cannot transition from RELEASED state"
        );
    }
}
