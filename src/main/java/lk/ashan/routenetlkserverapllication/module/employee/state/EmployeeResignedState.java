package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

/**
 * Represents the state of an employee who has resigned.
 * This state enforces rules for transitioning from the RESIGNED state.
 */
@Component
public class EmployeeResignedState implements EmployeeState {

    /**
     * Handles the transition of an employee from the RESIGNED state to a new state.
     *
     * @param employee  The employee whose state is being transitioned.
     * @param newStatus The new status to which the employee is transitioning.
     * @throws InvalidStateTransitionException if the transition from RESIGNED to the new status is invalid.
     */
    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("RESIGNED".equals(newStatusName)) return;

        throw new InvalidStateTransitionException(
            "Invalid status transition from RESIGNED to " + newStatusName
        );

    }
}
