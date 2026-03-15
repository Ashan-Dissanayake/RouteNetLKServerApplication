package lk.ashan.routenetlkserverapllication.module.permit.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class PermitStateFactory {

    private final Map<String, Supplier<PermitState>> stateMap;

    public PermitStateFactory() {
        stateMap = Map.of(
                "ACTIVE", PermiteActiveState::new,
                "EXPIRED", PermiteExpiredState::new,
                "SUSPENDED", PermiteSuspendedState::new,
                "TRANSFERRED", PermiteTransferredState::new
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
