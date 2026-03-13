package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.DriverUpdateRequestDto;

public interface DriverValidationStrategy {
    void validateCreate(DriverCreateRequestDto request);
    void validateUpdate(DriverUpdateRequestDto request);
}
