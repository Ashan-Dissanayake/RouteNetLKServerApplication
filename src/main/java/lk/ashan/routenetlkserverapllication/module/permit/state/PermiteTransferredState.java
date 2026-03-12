package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class PermiteTransferredState implements PermitState {

    @Override
    public void transitionTo(Permite permite, Permitestatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("TRANSFERRED".equals(newStatusName)) return;

        throw new InvalidStateTransitionException(
                "No transitions allowed from TRANSFERRED"
        );
    }
}

