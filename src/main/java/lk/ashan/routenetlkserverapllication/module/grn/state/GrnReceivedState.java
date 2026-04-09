package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class GrnReceivedState implements GrnState {
    @Override
    public void transitionTo(Grn grn, GrnStatus newStatus) {
        // RECEIVED is final. No transitions allowed.
        throw new InvalidStateTransitionException("A fully Received GRN is locked and cannot change status.");
    }
}
