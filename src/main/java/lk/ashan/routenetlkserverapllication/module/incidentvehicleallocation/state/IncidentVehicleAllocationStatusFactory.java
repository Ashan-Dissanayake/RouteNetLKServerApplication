package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class IncidentVehicleAllocationStatusFactory {

    private final Map<String, Supplier<IncidentVehicleAllocationState>> stateMap;

    public IncidentVehicleAllocationStatusFactory() {
        stateMap = Map.of(
                "ASSIGNED", AssignedState::new,
                "IN_PROGRESS", InProgressState::new,
                "RELEASED", ReleasedState::new,
                "CANCELLED", CancelledState::new
        );
    }


    public IncidentVehicleAllocationState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<IncidentVehicleAllocationState> supplier = stateMap.get(normalized);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown incident status: " + statusName);
        }
        return supplier.get();
    }
}
