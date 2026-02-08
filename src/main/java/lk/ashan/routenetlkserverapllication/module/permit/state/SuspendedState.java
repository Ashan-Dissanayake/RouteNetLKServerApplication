package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;

import java.util.List;

public class SuspendedState implements PermitState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "EXPIRED");

    @Override
    public void transitionTo(Permite permite, Permitestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("SUSPENDED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStatusTransitionException(
                    "Invalid status transition from SUSPENDED to " + newStatusName
            );
        }
        permite.setPermitestatus(newStatus);
    }
}

