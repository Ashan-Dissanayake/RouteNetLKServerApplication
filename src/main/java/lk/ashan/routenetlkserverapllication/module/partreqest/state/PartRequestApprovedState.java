package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PartRequestApprovedState implements PartRequestState {

    private static final List<String> ALLOWED = List.of("COMPLETED");

    @Override
    public void transitionTo(PartRequest request, PartRequestStatus newStatus) {

        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from APPROVED to " + newStatus.getName()
            );
        }

        request.setPartrequeststatus(newStatus);
    }
}
