package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employeestatus;

public interface EmployeeState {
    void transitionTo(Employee employee, Employeestatus newStatus);
}
