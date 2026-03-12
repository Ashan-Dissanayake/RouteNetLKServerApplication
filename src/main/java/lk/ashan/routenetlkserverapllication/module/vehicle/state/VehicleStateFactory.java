package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class VehicleStateFactory {

    private final Map<String, Supplier<VehicleState>> stateMap;

    public VehicleStateFactory() {
        // Initialize map with suppliers for each state
        stateMap = Map.of(
            "AVAILABLE", VehicleAvailableState::new,
            "IN SERVICE", VehicleInServiceState::new,
            "UNDER MAINTENANCE", VehicleUnderMaintenanceState::new,
            "OUT OF SERVICE", VehicleOutOfServiceState::new,
            "RESERVED", VehicleReservedState::new,
            "DECOMMISSIONED", VehicleDecommissionedState::new
        );
    }

    public VehicleState getState(String statusName) {
        Supplier<VehicleState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
