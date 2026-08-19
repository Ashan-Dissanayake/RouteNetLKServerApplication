package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

/**
 * Represents a state in the GRN (Goods Received Note) lifecycle.
 * Provides methods for transitioning between states and validating initial states.
 */
public interface GrnState {

    /**
     * Transitions the given GRN to a new status.
     *
     * @param grn the GRN entity to transition
     * @param newStatus the new status to transition to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    void transitionTo(Grn grn, GrnStatus newStatus);

    /**
     * Validates if the current state can be used as the initial state.
     *
     * @throws InvalidStateTransitionException if the state is not allowed as the initial state
     */
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
