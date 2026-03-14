package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class EmployeeResignedState implements EmployeeState {

    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("RESIGNED".equals(newStatusName)) return;
        throw new InvalidStateTransitionException(
            "Invalid status transition from RESIGNED to " + newStatusName
        );
    }
}
