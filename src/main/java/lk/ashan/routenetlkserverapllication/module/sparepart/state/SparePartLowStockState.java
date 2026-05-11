package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SparePartLowStockState implements SparePartState {

    private static final List<String> ALLOWED = List.of("AVAILABLE", "OUT OF STOCK", "DECOMMISSIONED");

    @Override
    public void transitionTo(Part part, Partstatus newStatus) {
        if ("LOW STOCK".equalsIgnoreCase(newStatus.getName())) return;
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from LOW STOCK to " + newStatus.getName()
            );
        }
        part.setPartstatus(newStatus);
    }
}
