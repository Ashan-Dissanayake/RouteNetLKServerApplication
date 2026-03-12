package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class VehicleServiceStateFactory {

    private final Map<String, Supplier<VehicleServiceState>> stateMap;

    public VehicleServiceStateFactory() {

        stateMap = Map.of(
                "CREATED", VehicleServiceCreatedState::new,
                "SCHEDULED", VehicleServiceScheduledState::new,
                "IN PROGRESS", VehicleServiceInProgressState::new,
                "COMPLETED", VehicleServiceCompletedState::new,
                "CANCELLED", VehicleServiceCancelledState::new
        );
    }

    public VehicleServiceState getState(String statusName) {

        String normalized = statusName.trim().toUpperCase();

        Supplier<VehicleServiceState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown vehicle service status: " + statusName
            );
        }

        return supplier.get();
    }

}
