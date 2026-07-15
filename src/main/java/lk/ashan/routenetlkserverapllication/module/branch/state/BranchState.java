package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

/**
 * Represents the state of a branch and provides methods for state transitions.
 */
public interface BranchState {

    /**
     * Transitions the branch to the specified target status.
     *
     * @param branch the branch to transition
     * @param targetStatus the target status to transition to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    void transitionTo(Branch branch, BranchStatus targetStatus);

    /**
     * Validates if the current state can be used as the initial branch status.
     *
     * @throws InvalidStateTransitionException if the state cannot be used as the initial status
     */
    default void validateInitial() {
        throw new InvalidStateTransitionException(
                "This state cannot be used as initial branch status"
        );
    }
}
