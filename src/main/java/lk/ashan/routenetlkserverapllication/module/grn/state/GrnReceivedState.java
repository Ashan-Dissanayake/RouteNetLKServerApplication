package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

/**
 * Represents the state of a GRN (Goods Received Note) when it is fully received.
 * This state is final, and no further transitions are allowed.
 */
@Component
public class GrnReceivedState implements GrnState {

    /**
     * Handles the transition of the GRN to a new status.
     * Since the GRN is in the RECEIVED state, no transitions are allowed.
     *
     * @param grn the GRN entity to transition
     * @param newStatus the new status to transition to
     * @throws InvalidStateTransitionException if a transition is attempted from the RECEIVED state
     */
    @Override
    public void transitionTo(Grn grn, GrnStatus newStatus) {
        // RECEIVED is final. No transitions allowed.
        throw new InvalidStateTransitionException("A fully Received GRN is locked and cannot change status.");
    }
}
