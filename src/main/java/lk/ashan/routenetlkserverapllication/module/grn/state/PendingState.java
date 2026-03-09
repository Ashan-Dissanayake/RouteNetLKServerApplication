package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.Grnstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PendingState implements GrnState {

    private static final List<String> ALLOWED =
            List.of("COMPLETED", "CANCELLED");

    @Override
    public void transitionTo(Grn grn, Grnstatus newStatus) {

        if (!ALLOWED.contains(newStatus.getName())) {

            throw new InvalidStateTransitionException(
                    "Invalid transition from PENDING to " + newStatus.getName()
            );
        }

        grn.setGrnstatus(newStatus);
    }
}
