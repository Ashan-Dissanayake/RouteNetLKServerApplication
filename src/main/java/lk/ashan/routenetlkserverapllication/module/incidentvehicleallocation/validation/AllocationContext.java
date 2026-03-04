package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.Branch;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AllocationContext {

    private final Incident incident;
    private final Vehicle vehicle;
    private final Branch providingBranch;
}
