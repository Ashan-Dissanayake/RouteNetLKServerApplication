package lk.ashan.routenetlkserverapllication.module.permit.state;


import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class PermiteSuspendedState implements PermitState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "EXPIRED");

    @Override
    public void transitionTo(Permite permite, PermiteStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("SUSPENDED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from SUSPENDED to " + newStatusName
            );
        }
        permite.setPermitestatus(newStatus);
    }
}

