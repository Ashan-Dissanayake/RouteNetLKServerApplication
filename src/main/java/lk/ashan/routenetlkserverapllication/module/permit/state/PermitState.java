package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.model.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.Permitestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface PermitState {
    void transitionTo(Permite permit, Permitestatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
