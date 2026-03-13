package lk.ashan.routenetlkserverapllication.module.crew.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.crew.model.dto.ConductorUpdateRequestDto;

public interface ConductorValidationStrategy {
    void validateCreate(ConductorCreateRequestDto request);
    void validateUpdate(ConductorUpdateRequestDto request);
}
