package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface PermitState {
    void transitionTo(Permite permite, PermiteStatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
