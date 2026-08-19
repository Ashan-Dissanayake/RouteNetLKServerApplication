package lk.ashan.routenetlkserverapllication.module.employee.state;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Factory class for creating instances of {@link EmployeeState} based on the given status name.
 * This class uses a map to associate status names with their corresponding {@link EmployeeState} implementations.
 */
@Component
public class EmployeeStateFactory {

    private final Map<String, Supplier<EmployeeState>> stateMap;

    /**
     * Constructs an instance of {@code EmployeeStateFactory} and initializes the state map
     * with predefined mappings of status names to their respective {@link EmployeeState} suppliers.
     */
    public EmployeeStateFactory() {
        stateMap = Map.of(
            "ACTIVE", EmployeeActiveState::new,
            "SUSPEND", EmployeeSuspendState::new,
            "ON LEAVE", EmployeeOnLeaveState::new,
            "RESIGNED", EmployeeResignedState::new
        );
    }

    /**
     * Retrieves the {@link EmployeeState} instance corresponding to the given status name.
     *
     * @param statusName the name of the employee status (case-insensitive).
     * @return the {@link EmployeeState} instance associated with the given status name.
     * @throws IllegalArgumentException if the given status name is not recognized.
     */
    public EmployeeState getState(String statusName) {
        Supplier<EmployeeState> supplier = stateMap.get(statusName.toUpperCase());
        if (supplier == null) {
            throw new IllegalArgumentException("Unknown status: " + statusName);
        }
        return supplier.get();
    }
}
