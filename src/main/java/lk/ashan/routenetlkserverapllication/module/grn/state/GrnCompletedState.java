package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grnstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class GrnCompletedState implements GrnState {

    @Override
    public void transitionTo(Grn grn, Grnstatus newStatus) {

        throw new InvalidStateTransitionException(
                "Completed GRNs cannot transition to another state"
        );
    }
}
