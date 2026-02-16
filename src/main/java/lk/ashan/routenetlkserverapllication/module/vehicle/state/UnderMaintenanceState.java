package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class UnderMaintenanceState implements VehicleState {
    
    private static final List<String> ALLOWED = List.of("AVAILABLE", "OUT OF SERVICE", "DECOMMISSIONED");

    @Override
    public void transitionTo(Vehicle vehicle, Vehiclestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("UNDER MAINTENANCE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
             throw new InvalidStateTransitionException(
                "Invalid status transition from UNDER MAINTENANCE to " + newStatusName
            );
        }
    }
}
