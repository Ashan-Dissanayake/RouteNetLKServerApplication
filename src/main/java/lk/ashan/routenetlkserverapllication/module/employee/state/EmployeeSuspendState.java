package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the suspended state of an employee.
 * This state allows transitions to specific statuses only.
 */
@Component
public class EmployeeSuspendState implements EmployeeState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "RESIGNED");

    /**
     * Handles the transition of an employee from the SUSPEND state to a new status.
     *
     * @param employee   The employee whose status is being transitioned.
     * @param newStatus  The new status to transition to.
     * @throws InvalidStateTransitionException if the transition to the new status is not allowed.
     */
    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("SUSPEND".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
             throw new InvalidStateTransitionException(
                "Invalid status transition from SUSPEND to " + newStatusName
            );
        }
        employee.setEmployeestatus(newStatus);
    }

}
