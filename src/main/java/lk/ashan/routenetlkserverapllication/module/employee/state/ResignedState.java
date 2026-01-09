package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.Collections;
import java.util.List;

public class ResignedState implements EmployeeState {
    
    private static final List<String> ALLOWED = Collections.emptyList();

    @Override
    public void transitionTo(Employee employee, Employeestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("RESIGNED".equals(newStatusName)) return;

        throw new InvalidStatusTransitionException(
            "Invalid status transition from RESIGNED to " + newStatusName
        );
    }
}
