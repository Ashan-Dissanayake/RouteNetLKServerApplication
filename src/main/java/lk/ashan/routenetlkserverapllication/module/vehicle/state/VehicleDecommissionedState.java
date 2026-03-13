package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.Collections;
import java.util.List;

public class VehicleDecommissionedState implements VehicleState {
    
    private static final List<String> ALLOWED = Collections.emptyList();

    @Override
    public void transitionTo(Vehicle vehicle, Vehiclestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("DECOMMISSIONED".equals(newStatusName)) return;

        // No transitions allowed from DECOMMISSIONED
        throw new InvalidStateTransitionException(
            "Invalid status transition from DECOMMISSIONED to " + newStatusName
        );
    }
}
