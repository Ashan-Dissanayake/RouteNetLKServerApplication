package lk.ashan.routenetlkserverapllication.module.vehicle.validation;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;

public interface VehicleValidationStrategy {
    void validateCreate(VehicleCreateRequestDto request);
    void validateUpdate(VehicleUpdateRequestDto request);
}
