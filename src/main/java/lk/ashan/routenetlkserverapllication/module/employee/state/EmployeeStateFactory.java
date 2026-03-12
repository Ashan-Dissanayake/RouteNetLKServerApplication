package lk.ashan.routenetlkserverapllication.module.employee.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

@Component
public class EmployeeStateFactory {

    private final Map<String, Supplier<EmployeeState>> stateMap;

    public EmployeeStateFactory() {
        stateMap = Map.of(
            "ACTIVE", EmployeeActiveState::new,
            "SUSPEND", EmployeeSuspendState::new,
            "ON LEAVE", EmployeeOnLeaveState::new,
            "RESIGNED", EmployeeResignedState::new
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
