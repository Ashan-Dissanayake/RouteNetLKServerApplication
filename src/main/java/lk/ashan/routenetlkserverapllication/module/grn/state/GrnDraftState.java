package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GrnDraftState implements GrnState {
    private static final List<String> ALLOWED = List.of("PARTIALLY RECEIVED", "RECEIVED");

    @Override
    public void transitionTo(Grn grn, GrnStatus newStatus) {
        String newStatusName = newStatus.getName().toUpperCase();
        if ("DRAFT".equals(newStatusName)) return;

        if (!ALLOWED.contains(newStatusName)) {
            throw new InvalidStateTransitionException("Draft GRN can only move to Partial or Received.");
        }
        grn.setGrnstatus(newStatus);
    }
}
