package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrnPartiallyReceivedState implements GrnState {
    private static final List<String> ALLOWED = List.of("PARTIALLY RECEIVED", "RECEIVED");

    @Override
    public void transitionTo(Grn grn, GrnStatus newStatus) {
        String newStatusName = newStatus.getName().toUpperCase();

        // If the system creates a second GRN for a partial delivery,
        // it may stay partial or finally become received.
        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException("Cannot move from Partial back to Draft.");
        }
        grn.setGrnstatus(newStatus);
    }
}
