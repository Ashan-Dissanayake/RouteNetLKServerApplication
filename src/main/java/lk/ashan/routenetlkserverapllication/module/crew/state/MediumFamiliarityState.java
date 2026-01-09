package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

public class MediumFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, Routefamiliaritylevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("MEDIUM".equals(newName)) return;
        if ("HIGH".equals(newName)) return; // Allowed upgrade

        throw new InvalidStatusTransitionException(
             "Invalid route familiarity transition from MEDIUM to " + newName
        );
    }
}
