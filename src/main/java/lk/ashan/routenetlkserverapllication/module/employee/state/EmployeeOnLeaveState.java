package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * Represents the state of an employee who is currently on leave.
 * This state allows transitions to specific other states.
 */
@Component
public class EmployeeOnLeaveState implements EmployeeState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "RESIGNED");

    /**
     * Transitions the employee to a new status if the transition is valid.
     *
     * @param employee the employee whose status is being transitioned
     * @param newStatus the new status to transition the employee to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ON LEAVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
             throw new InvalidStateTransitionException(
                "Invalid status transition from ON LEAVE to " + newStatusName
            );
        }

        employee.setEmployeestatus(newStatus);

    }
}
