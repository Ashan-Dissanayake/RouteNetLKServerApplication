package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class IncidentVehicleAllocationStatusFactory {

    private final Map<String, Supplier<IncidentVehicleAllocationState>> stateMap;

    public IncidentVehicleAllocationStatusFactory() {
        stateMap = Map.of(
                "ASSIGNED", IncidentVehicleAllocationAssignedState::new,
                "IN PROGRESS", IncidentVehicleAllocationInProgressState::new,
                "RELEASED", IncidentVehicleAllocationReleasedState::new,
                "CANCELLED", IncidentVehicleAllocationCancelledState::new
        );
    }

    public IncidentVehicleAllocationState getState(String statusName) {
        if (statusName == null) throw new IllegalArgumentException("Status name cannot be null");

        String normalized = statusName.trim().toUpperCase();
        Supplier<IncidentVehicleAllocationState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown allocation status: " + statusName);
        }
        return supplier.get();
    }
}
