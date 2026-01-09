package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleUpdateRequestDto;

public interface VehicleValidationStrategy {
    void validateCreate(VehicleCreateRequestDto request);
    void validateUpdate(VehicleUpdateRequestDto request);
}
