package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.Partrequeststatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PendingState implements PartRequestState {

    private static final List<String> ALLOWED =
            List.of("APPROVED", "REJECTED");

    @Override
    public void transitionTo(Partrequest request, Partrequeststatus newStatus) {

        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from PENDING to " + newStatus.getName()
            );
        }

        request.setPartrequeststatus(newStatus);
    }

    @Override
    public void validateInitial() { }
}
