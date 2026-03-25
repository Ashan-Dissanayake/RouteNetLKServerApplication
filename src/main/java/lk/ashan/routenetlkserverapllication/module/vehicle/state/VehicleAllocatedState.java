package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class VehicleAllocatedState implements VehicleState {

    private static final List<String> ALLOWED = List.of("IN OPERATION", "AVAILABLE", "BREAKDOWN");

    @Override
    public void transitionTo(Vehicle vehicle, VehicleStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ALLOCATED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from ALLOCATED to " + newStatusName
            );
        }
        vehicle.setVehiclestatus(newStatus);
    }
}
