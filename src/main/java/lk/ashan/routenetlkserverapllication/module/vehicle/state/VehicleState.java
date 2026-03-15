package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface VehicleState {
    void transitionTo(Vehicle vehicle, VehicleStatus newStatus);
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial status"
        );
    }
}
