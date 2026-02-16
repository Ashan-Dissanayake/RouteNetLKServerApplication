package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class OutOfServiceState implements VehicleState {
    
    private static final List<String> ALLOWED = List.of("UNDER MAINTENANCE", "DECOMMISSIONED");

    @Override
    public void transitionTo(Vehicle vehicle, Vehiclestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("OUT OF SERVICE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
             throw new InvalidStateTransitionException(
                "Invalid status transition from OUT OF SERVICE to " + newStatusName
            );
        }
    }
}
