package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface PartRequestState {

    void transitionTo(Partrequest request, Partrequeststatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
