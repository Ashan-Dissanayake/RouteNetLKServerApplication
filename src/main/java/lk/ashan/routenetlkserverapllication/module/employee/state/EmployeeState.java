package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface EmployeeState {
    void transitionTo(Employee employee, EmployeeStatus newStatus);
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial employee status"
        );
    }
}
