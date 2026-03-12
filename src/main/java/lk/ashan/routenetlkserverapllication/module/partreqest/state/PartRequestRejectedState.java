package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class PartRequestRejectedState implements PartRequestState {

    @Override
    public void transitionTo(Partrequest request, Partrequeststatus newStatus) {
        throw new InvalidStateTransitionException(
                "Rejected requests cannot transition to another state"
        );
    }
}
