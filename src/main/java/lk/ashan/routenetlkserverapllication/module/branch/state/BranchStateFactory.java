package lk.ashan.routenetlkserverapllication.module.branch.state;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class BranchStateFactory {

    private final Map<String, Supplier<BranchState>> stateMap;

    public BranchStateFactory() {
        stateMap = Map.of(
                "ACTIVE",BranchActiveState::new,
                "SUSPENDED", BranchSuspendedState::new,
                "CLOSED",  BranchClosedState::new
        );
    }

    public BranchState getState(String statusName) {
        String normalized = statusName.trim().toUpperCase();
        Supplier<BranchState> supplier = stateMap.get(normalized);
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown branch status: " + statusName);
        }
        return supplier.get();
    }
}
