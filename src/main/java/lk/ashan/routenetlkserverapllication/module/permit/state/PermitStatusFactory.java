package lk.ashan.routenetlkserverapllication.module.permit.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class PermitStatusFactory {

    private final Map<String, Supplier<PermitState>> stateMap;

    public PermitStatusFactory() {
        stateMap = Map.of(
                "ACTIVE", ActiveState::new,
                "EXPIRED", ExpiredState::new,
                "SUSPENDED", SuspendedState::new,
                "TRANSFERRED", TransferredState::new
        );
    }

    public PermitState getState(String statusName) {
        Supplier<PermitState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
