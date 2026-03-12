package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class PermiteExpiredState implements PermitState {

    private static final List<String> ALLOWED = List.of("ACTIVE"); // RENEWAL

    @Override
    public void transitionTo(Permite permite, Permitestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("EXPIRED".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from EXPIRED to " + newStatusName
            );
        }
        permite.setPermitestatus(newStatus);
    }
}

