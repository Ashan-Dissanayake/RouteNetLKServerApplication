package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface VehicleServiceState {

    void transitionTo(Vehicleservice service, Vehicleservicestatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }

}
