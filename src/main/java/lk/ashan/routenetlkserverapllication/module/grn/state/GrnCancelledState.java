package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class GrnCancelledState implements GrnState {

    @Override
    public void transitionTo(Grn grn, GrnStatus newStatus) {

        throw new InvalidStateTransitionException(
                "Cancelled GRNs cannot transition to another state"
        );
    }
}
