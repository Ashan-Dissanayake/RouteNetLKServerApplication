package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;

public interface RosterValidationStrategy {
    void validateCreate(RosterCreateRequestDto request);
}
