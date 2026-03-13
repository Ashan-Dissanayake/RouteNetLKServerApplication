package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employeestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.Collections;
import java.util.List;

public class EmployeeResignedState implements EmployeeState {
    
    private static final List<String> ALLOWED = Collections.emptyList();

    @Override
    public void transitionTo(Employee employee, Employeestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("RESIGNED".equals(newStatusName)) return;

        throw new InvalidStateTransitionException(
            "Invalid status transition from RESIGNED to " + newStatusName
        );
    }
}
