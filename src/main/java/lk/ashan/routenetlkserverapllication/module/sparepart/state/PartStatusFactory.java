package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class PartStatusFactory {

    private final Map<String, Supplier<SparePartState>> stateMap;

    public PartStatusFactory() {
        stateMap = Map.of(
                "AVAILABLE", SparePartAvailableState::new,
                "LOW_STOCK", SparePartLowStockState::new,
                "OUT_OF_STOCK", SparePartOutOfStockState::new,
                "DECOMMISSIONED", SparePartDecommissionedState::new
        );
    }

    public SparePartState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<SparePartState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown part status: " + statusName);
        }

        return supplier.get();
    }
}
