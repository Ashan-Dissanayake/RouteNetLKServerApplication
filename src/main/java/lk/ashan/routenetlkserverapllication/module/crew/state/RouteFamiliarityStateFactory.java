package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class RouteFamiliarityStateFactory {

    private final Map<String, Supplier<RouteFamiliarityState>> stateMap;

    public RouteFamiliarityStateFactory() {
        stateMap = Map.of(
            "LOW", LowFamiliarityState::new,
            "MEDIUM", MediumFamiliarityState::new,
            "HIGH", HighFamiliarityState::new
        );
    }

    public RouteFamiliarityState getState(String levelName) {
        Supplier<RouteFamiliarityState> supplier = stateMap.get(levelName.toUpperCase());
        if (supplier == null) {
            throw new ResourceNotFoundException("Unknown route familiarity level: " + levelName);
        }
        return supplier.get();
    }
}
