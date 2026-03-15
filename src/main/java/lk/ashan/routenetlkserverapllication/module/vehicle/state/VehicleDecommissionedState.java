package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.Collections;
import java.util.List;

public class VehicleDecommissionedState implements VehicleState {
    
    private static final List<String> ALLOWED = Collections.emptyList();

    @Override
    public void transitionTo(Vehicle vehicle, VehicleStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        // No transitions allowed from DECOMMISSIONED
        throw new InvalidStateTransitionException(
            "Invalid status transition from DECOMMISSIONED to " + newStatusName
        );
    }
}
