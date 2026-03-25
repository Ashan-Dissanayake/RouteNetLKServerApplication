package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.function.Supplier;

@Component
public class VehicleStateFactory {

    private final Map<String, Supplier<VehicleState>> stateMap;

    public VehicleStateFactory() {
        // Initialize map with suppliers for each updated state
        stateMap = Map.of(
                "AVAILABLE", VehicleAvailableState::new,
                "ALLOCATED", VehicleAllocatedState::new,
                "IN OPERATION", VehicleInOperationState::new,
                "MAINTENANCE", VehicleMaintenanceState::new,
                "BREAKDOWN", VehicleBreakdownState::new,
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
