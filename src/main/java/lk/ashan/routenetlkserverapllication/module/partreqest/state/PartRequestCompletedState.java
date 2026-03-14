package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class PartRequestCompletedState implements PartRequestState {

    @Override
    public void transitionTo(PartRequest request, PartRequestStatus newStatus) {
        throw new InvalidStateTransitionException(
                "Completed requests cannot transition to another state"
        );
    }
}
