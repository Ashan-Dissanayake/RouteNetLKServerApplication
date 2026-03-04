package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.Incidentvehicleallocationstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface IncidentVehicleAllocationState {
    void transitionTo(Incidentvehicleallocation allocation,
                      Incidentvehicleallocationstatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
