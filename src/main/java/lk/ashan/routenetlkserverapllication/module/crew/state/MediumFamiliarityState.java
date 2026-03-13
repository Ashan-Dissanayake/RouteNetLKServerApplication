package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class MediumFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, Routefamiliaritylevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("MEDIUM".equals(newName)) return;
        if ("HIGH".equals(newName)) return; // Allowed upgrade

        throw new InvalidStateTransitionException(
             "Invalid route familiarity transition from MEDIUM to " + newName
        );
    }
}
