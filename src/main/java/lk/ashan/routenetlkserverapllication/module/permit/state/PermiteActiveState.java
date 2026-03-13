package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permitestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class PermiteActiveState implements PermitState {

    private static final List<String> ALLOWED = List.of("EXPIRED", "SUSPENDED", "TRANSFERRED");

    @Override
    public void transitionTo(Permite permite, Permitestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("ACTIVE".equals(newStatusName)) return;
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from ACTIVE to " + newStatusName
            );
        }
        permite.setPermitestatus(newStatus);
    }

    @Override
    public void validateInitial() {
        System.out.println("Validate initial called");
    }
}

