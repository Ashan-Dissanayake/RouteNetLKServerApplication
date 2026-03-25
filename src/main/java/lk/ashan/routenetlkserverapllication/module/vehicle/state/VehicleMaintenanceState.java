package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class VehicleMaintenanceState implements VehicleState {

    private static final List<String> ALLOWED = List.of("AVAILABLE", "DECOMMISSIONED");

    @Override
    public void transitionTo(Vehicle vehicle, VehicleStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("MAINTENANCE".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from MAINTENANCE to " + newStatusName
            );
        }
        vehicle.setVehiclestatus(newStatus);
    }
}
