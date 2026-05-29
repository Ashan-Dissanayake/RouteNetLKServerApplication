package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeActiveState implements EmployeeState {
    
    private static final List<String> ALLOWED = List.of("SUSPEND", "RESIGNED", "ON LEAVE");

    @Override
    public void transitionTo(Employee employee, EmployeeStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ACTIVE".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                "Invalid status transition from ACTIVE to " + newStatusName
            );
        }
        employee.setEmployeestatus(newStatus);
    }

    @Override
    public void validateInitial() {}
}
