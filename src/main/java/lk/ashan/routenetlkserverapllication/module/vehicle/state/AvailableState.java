package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class AvailableState implements VehicleState {
    
    private static final List<String> ALLOWED = List.of("IN SERVICE", "RESERVED", "UNDER MAINTENANCE");

    @Override
    public void transitionTo(Vehicle vehicle, Vehiclestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("AVAILABLE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                "Invalid status transition from AVAILABLE to " + newStatusName
            );
        }
    }
}
