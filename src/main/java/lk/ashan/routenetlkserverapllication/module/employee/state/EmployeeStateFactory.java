package lk.ashan.routenetlkserverapllication.module.employee.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class EmployeeStateFactory {

    private final Map<String, Supplier<EmployeeState>> stateMap;

    public EmployeeStateFactory() {
        stateMap = Map.of(
            "ACTIVE", ActiveState::new,
            "SUSPEND", SuspendState::new,
            "ON LEAVE", OnLeaveState::new,
            "RESIGNED", ResignedState::new
        );
    }

    public EmployeeState getState(String statusName) {
        Supplier<EmployeeState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
