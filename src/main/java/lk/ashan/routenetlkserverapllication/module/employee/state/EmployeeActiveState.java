package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the active state of an employee. This state allows transitions
 * to specific statuses such as SUSPEND, RESIGNED, or ON LEAVE.
 */
@Component
public class EmployeeActiveState implements EmployeeState {

    private static final List<String> ALLOWED = List.of("SUSPEND", "RESIGNED", "ON LEAVE");

    /**
     * Handles the transition of an employee from the ACTIVE state to a new status.
     *
     * @param employee The employee whose status is being transitioned.
     * @param newStatus The new status to transition to.
     * @throws InvalidStateTransitionException If the transition to the new status is not allowed.
     */
    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ACTIVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                "Invalid status transition from ACTIVE to " + newStatusName
            );
        }
        employee.setEmployeestatus(newStatus);
    }

    /**
     * Validates the initial state of the employee. This method is a no-op for the ACTIVE state.
     */
    @Override
    public void validateInitial() {}
}
