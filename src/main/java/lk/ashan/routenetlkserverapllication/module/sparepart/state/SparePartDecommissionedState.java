package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SparePartDecommissionedState implements SparePartState {

    private static final List<String> ALLOWED = List.of();

    @Override
    public void transitionTo(Part part, Partstatus newStatus) {
        throw new InvalidStateTransitionException(
                "No transitions allowed from DECOMMISSIONED state"
        );
    }

    @Override
    public void validateInitial() {
        throw new InvalidStateTransitionException(
                "DECOMMISSIONED cannot be an initial part status"
        );
    }
}
