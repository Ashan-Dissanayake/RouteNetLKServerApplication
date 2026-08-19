package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

/**
 * Represents the state of an employee and provides methods for state transitions.
 */
public interface EmployeeState {

    /**
     * Transitions the given employee to a new status.
     *
     * @param employee the employee whose state is being transitioned
     * @param newStatus the new status to transition the employee to
     */
    void transitionTo(Employee employee, EmployeeStatus newStatus);

    /**
     * Validates if the current state can be used as the initial employee status.
     *
     * @throws InvalidStateTransitionException if the state cannot be used as the initial status
     */
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial employee status"
        );
    }
}
