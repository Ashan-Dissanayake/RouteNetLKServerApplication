package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class CancelledState implements GrnState {

    @Override
    public void transitionTo(Grn grn, Grnstatus newStatus) {

        throw new InvalidStateTransitionException(
                "Cancelled GRNs cannot transition to another state"
        );
    }
}
