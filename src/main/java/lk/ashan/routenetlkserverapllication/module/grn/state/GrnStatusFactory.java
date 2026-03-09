package lk.ashan.routenetlkserverapllication.module.grn.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class GrnStatusFactory {

    private final Map<String, Supplier<GrnState>> stateMap;

    public GrnStatusFactory() {
        stateMap = Map.of(
                "PENDING", PendingState::new,
                "COMPLETED", CompletedState::new,
                "CANCELLED", CancelledState::new
        );
    }

    public GrnState getState(String statusName) {

        String normalized = statusName.trim().toUpperCase();

        Supplier<GrnState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown GRN status: " + statusName
            );
        }

        return supplier.get();
    }
}
