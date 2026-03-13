package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;

public interface RouteFamiliarityState {
    void transitionTo(Employee employee, Routefamiliaritylevel newLevel);
}
