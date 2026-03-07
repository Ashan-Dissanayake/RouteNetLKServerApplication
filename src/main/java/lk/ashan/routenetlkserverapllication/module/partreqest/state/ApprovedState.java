package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApprovedState implements PartRequestState {

    private static final List<String> ALLOWED =
            List.of("COMPLETED");

    @Override
    public void transitionTo(Partrequest request, Partrequeststatus newStatus) {

        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from APPROVED to " + newStatus.getName()
            );
        }

        request.setPartrequeststatus(newStatus);
    }
}
