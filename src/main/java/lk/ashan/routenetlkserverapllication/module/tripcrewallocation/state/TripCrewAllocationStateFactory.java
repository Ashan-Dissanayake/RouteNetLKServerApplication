package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class TripCrewAllocationStateFactory {

    private final Map<String, Supplier<TripCrewAllocationState>> stateMap;

    public TripCrewAllocationStateFactory() {
        stateMap = Map.of(
                "SUGGESTED", SuggestedState::new,
                "CONFIRMED", ConfirmedState::new,
                "REJECTED", RejectedState::new
        );
    }

    public TripCrewAllocationState getState(String statusName) {
        Supplier<TripCrewAllocationState> supplier =
                stateMap.get(statusName.trim().toUpperCase());

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }

        return supplier.get();
    }
}
