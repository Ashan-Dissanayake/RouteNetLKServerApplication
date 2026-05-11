package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SparePartOutOfStockState implements SparePartState {

    private static final List<String> ALLOWED = List.of("LOW STOCK", "DECOMMISSIONED");

    @Override
    public void transitionTo(Part part, Partstatus newStatus) {
        if ("OUT OF STOCK".equalsIgnoreCase(newStatus.getName())) return;
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from OUT_OF_STOCK to " + newStatus.getName()
            );
        }
        part.setPartstatus(newStatus);
    }
//
//    @Override
//    public void validateInitial() { }
}
