package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;

public interface RouteFamiliarityState {
    void transitionTo(Employee employee, Routefamiliaritylevel newLevel);
}
