package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public class PermiteTransferredState implements PermitState {

    @Override
    public void transitionTo(Permite permite, PermiteStatus newStatus) {
        String newStatusName = newStatus.getName().trim().toUpperCase();
        if ("TRANSFERRED".equals(newStatusName)) return;

        throw new InvalidStateTransitionException(
                "No transitions allowed from TRANSFERRED"
        );
    }
}

