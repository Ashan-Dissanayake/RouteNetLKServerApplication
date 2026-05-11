package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class TripExecutionStatusFactory {
    private final Map<String, Supplier<TripExecutionState>> stateMap;

    public TripExecutionStatusFactory() {
        stateMap = Map.of(
                "SCHEDULED", TripExecutionScheduledState::new,
                "DISPATCHED", TripExecutionDispatchedState::new,
                "CHECKED IN", TripExecutionCheckedInState::new,
                "ARRIVED", TripExecutionArrivedState::new,
                "BREAKDOWN", TripExecutionBreakdownState::new
        );
    }

    public TripExecutionState getState(String statusName) {
        return stateMap.getOrDefault(statusName.trim().toUpperCase(), () -> {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }).get();
    }
}
