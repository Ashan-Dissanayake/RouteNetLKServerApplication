package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

public interface BranchState {
    void transitionTo(Branch branch, BranchStatus targetStatus);

    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial branch status"
        );
    }
}
