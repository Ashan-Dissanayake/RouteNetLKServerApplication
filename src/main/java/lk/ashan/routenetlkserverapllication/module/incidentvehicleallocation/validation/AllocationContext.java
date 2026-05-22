package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AllocationContext {
    private final Integer incidentId;
    private final Integer vehicleId;
    private final Integer branchId;
}
