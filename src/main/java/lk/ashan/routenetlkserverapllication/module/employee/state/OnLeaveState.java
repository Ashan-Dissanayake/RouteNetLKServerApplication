package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class OnLeaveState implements EmployeeState {
    
    private static final List<String> ALLOWED = List.of("ACTIVE", "RESIGNED");

    @Override
    public void transitionTo(Employee employee, Employeestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ON LEAVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
             throw new InvalidStatusTransitionException(
                "Invalid status transition from ON LEAVE to " + newStatusName
            );
        }
    }
}
