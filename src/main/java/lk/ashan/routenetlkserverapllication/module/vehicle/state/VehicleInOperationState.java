package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class VehicleInOperationState implements VehicleState {

    private static final List<String> ALLOWED = List.of("AVAILABLE", "BREAKDOWN", "MAINTENANCE");

    @Override
    public void transitionTo(Vehicle vehicle, VehicleStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("IN OPERATION".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from IN OPERATION to " + newStatusName
            );
        }
        vehicle.setVehiclestatus(newStatus);

    }
}
