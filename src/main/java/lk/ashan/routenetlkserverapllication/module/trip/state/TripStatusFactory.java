package lk.ashan.routenetlkserverapllication.module.trip.state;

import lk.ashan.routenetlkserverapllication.module.permit.state.SuspendedState;
import lk.ashan.routenetlkserverapllication.module.permit.state.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class TripStatusFactory {

    private final Map<String, Supplier<TripState>> stateMap;

    public TripStatusFactory() {
        stateMap = Map.of(
                "PLANNED", PlannedState::new,
                "NEED VEHICLE OVERRIDE", NeedVehicleOverrideState::new,
                "IN PROGRESS", InProgressState::new,
                "DELAYED", DelayedState::new,
                "SUSPENDED", SuspendedState::new,
                "COMPLETE", CompletedState::new,
                "CANCELLED", CancelledState::new
        );
    }

    public TripState getState(String statusName) {
        Supplier<TripState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
