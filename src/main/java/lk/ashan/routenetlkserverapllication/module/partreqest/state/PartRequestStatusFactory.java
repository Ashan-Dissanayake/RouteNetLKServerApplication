package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class PartRequestStatusFactory {

    private final Map<String, Supplier<PartRequestState>> stateMap;

    public PartRequestStatusFactory() {
        stateMap = Map.of(
                "PENDING", PendingState::new,
                "APPROVED", ApprovedState::new,
                "REJECTED", RejectedState::new,
                "COMPLETED", CompletedState::new
        );
    }

    public PartRequestState getState(String statusName) {

        String normalized = statusName.trim().toUpperCase();
        Supplier<PartRequestState> supplier = stateMap.get(normalized);

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown request status: " + statusName
            );
        }

        return supplier.get();
    }
}
