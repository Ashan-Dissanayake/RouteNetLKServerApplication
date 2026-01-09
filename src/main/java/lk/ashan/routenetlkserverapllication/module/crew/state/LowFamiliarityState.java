package lk.ashan.routenetlkserverapllication.module.crew.state;

import lk.ashan.routenetlkserverapllication.module.crew.model.Routefamiliaritylevel;
import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

public class LowFamiliarityState implements RouteFamiliarityState {
    
    @Override
    public void transitionTo(Employee employee, Routefamiliaritylevel newLevel) {
        String newName = newLevel.getName().trim().toUpperCase();
        
        if ("LOW".equals(newName)) return;
        if ("MEDIUM".equals(newName)) return; // Allowed upgrade

        throw new InvalidStatusTransitionException(
             "Invalid route familiarity transition from LOW to " + newName
        );
    }
}
