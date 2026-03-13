package lk.ashan.routenetlkserverapllication.module.branch.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;

public interface BranchValidationStrategy {
    void validateCreate(BranchCreateRequestDto request);
    void validateUpdate(BranchUpdateRequestDto request);
}
