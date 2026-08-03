package lk.ashan.routenetlkserverapllication.module.permit.event;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;

public record PermitTransferredEvent(
        Permite permit,
        Vehicle vehicle,
        Branch branch
) {}
