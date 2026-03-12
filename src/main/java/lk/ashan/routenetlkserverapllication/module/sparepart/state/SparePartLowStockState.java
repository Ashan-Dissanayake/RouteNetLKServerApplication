package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SparePartLowStockState implements SparePartState {

    private static final List<String> ALLOWED = List.of("AVAILABLE", "OUT_OF_STOCK", "DECOMMISSIONED");

    @Override
    public void transitionTo(Part part, Partstatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from LOW_STOCK to " + newStatus.getName()
            );
        }
        part.setPartstatus(newStatus);
    }

    @Override
    public void validateInitial() { }
}
