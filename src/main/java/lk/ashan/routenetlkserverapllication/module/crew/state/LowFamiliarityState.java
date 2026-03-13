package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class LowFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, RouteFamiliarityLevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("LOW".equals(newName)) return;
        if ("MEDIUM".equals(newName)) return; // Allowed upgrade

        throw new InvalidStateTransitionException(
             "Invalid route familiarity transition from LOW to " + newName
        );
    }
}
