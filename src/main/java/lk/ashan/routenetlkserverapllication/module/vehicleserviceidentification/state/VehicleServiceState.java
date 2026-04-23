package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface VehicleServiceState {

    void transitionTo(VehicleService service, VehicleServiceStatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }

}
