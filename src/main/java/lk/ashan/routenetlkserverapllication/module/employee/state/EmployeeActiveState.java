package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class EmployeeActiveState implements EmployeeState {
    
    private static final List<String> ALLOWED = List.of("SUSPEND", "RESIGNED", "ON LEAVE");

    @Override
    public void transitionTo(Employee employee, Employeestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ACTIVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                "Invalid status transition from ACTIVE to " + newStatusName
            );
        }
    }
}
