package lk.ashan.routenetlkserverapllication.module.vehicleservice.validation;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class VehicleServiceValidationContext {
    private final Integer branchId;
    private final Integer vehicleId;
    private final String serviceTypeName;
    private final Integer incidentId;
    private final List<VehicleServicePartDto> parts;
}
