package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class HighFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, Routefamiliaritylevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("HIGH".equals(newName)) return;

        // No upgrades from HIGH
        throw new InvalidStateTransitionException(
             "Invalid route familiarity transition from HIGH to " + newName
        );
    }
}
