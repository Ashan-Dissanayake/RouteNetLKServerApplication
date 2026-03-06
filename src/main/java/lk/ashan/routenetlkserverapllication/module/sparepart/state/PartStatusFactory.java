package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class PartStatusFactory {

    private final Map<String, Supplier<PartState>> stateMap;

    public PartStatusFactory() {
        stateMap = Map.of(
                "AVAILABLE", AvailableState::new,
                "LOW_STOCK", LowStockState::new,
                "OUT_OF_STOCK", OutOfStockState::new,
                "DECOMMISSIONED", DecommissionedState::new
        );
    }

    public PartState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<PartState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown part status: " + statusName);
        }

        return supplier.get();
    }
}
