package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface SparePartState {
    void transitionTo(Part part, Partstatus newStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state is not allowed as initial state"
        );
    }
}
