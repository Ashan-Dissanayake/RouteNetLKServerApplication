package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;

public interface RouteFamiliarityState {
    void transitionTo(Employee employee, RouteFamiliarityLevel newLevel);
}
