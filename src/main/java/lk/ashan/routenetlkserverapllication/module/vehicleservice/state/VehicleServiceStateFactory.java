package lk.ashan.routenetlkserverapllication.module.vehicleservice.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class VehicleServiceStateFactory {

    private final Map<String, Supplier<VehicleServiceState>> stateMap;

    public VehicleServiceStateFactory() {
        stateMap = Map.of(
                "PENDING", VehicleServicePendingState::new,
                "SCHEDULED", VehicleServiceScheduledState::new,
                "IN_PROGRESS", VehicleServiceInProgressState::new,
                "ON_HOLD_PARTS", VehicleServiceOnHoldPartsState::new,
                "COMPLETED", VehicleServiceCompletedState::new,
                "CANCELLED", VehicleServiceCancelledState::new
        );
    }

    public VehicleServiceState getState(String statusName) {
        if (statusName == null) throw new IllegalArgumentException("Status name cannot be null");

        String normalized = statusName.trim().toUpperCase().replace(" ", "_");
        Supplier<VehicleServiceState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown vehicle service status: " + statusName);
        }
        return supplier.get();
    }
}
