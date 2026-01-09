package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.dto.DriverUpdateRequestDto;

public interface DriverValidationStrategy {
    void validateCreate(DriverCreateRequestDto request);
    void validateUpdate(DriverUpdateRequestDto request);
}
