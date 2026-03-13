package lk.ashan.routenetlkserverapllication.module.employee.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employeestatus;

public interface EmployeeState {
    void transitionTo(Employee employee, Employeestatus newStatus);
}
