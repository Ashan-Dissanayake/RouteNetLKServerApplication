package lk.ashan.routenetlkserverapllication.module.crew.state.routefamility;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.RouteFamiliarityLevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class MediumFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, RouteFamiliarityLevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("MEDIUM".equals(newName)) return;
        if ("HIGH".equals(newName)) return; // Allowed upgrade

        throw new InvalidStateTransitionException(
             "Invalid route familiarity transition from MEDIUM to " + newName
        );
    }
}
