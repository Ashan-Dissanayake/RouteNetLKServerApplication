package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the suspended state of a branch. This state allows transitions
 * to specific states and validates the initial state of the branch.
 */
@Component
public class BranchSuspendedState implements BranchState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "CLOSED");

    /**
     * Transitions the branch to the specified target status if the transition is valid.
     *
     * @param branch       the branch whose status is to be transitioned
     * @param targetStatus the target status to transition to
     * @throws InvalidStateTransitionException if the transition is not allowed
     */
    @Override
    public void transitionTo(Branch branch, BranchStatus targetStatus) {
        if (branch.getBranchstatus().getName().equalsIgnoreCase(targetStatus.getName())) {
            return;
        }
        if (!ALLOWED.contains(targetStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from SUSPENDED to " + targetStatus.getName()
            );
        }
        branch.setBranchstatus(targetStatus);
    }

    /**
     * Validates that the suspended state cannot be an initial branch status.
     *
     * @throws InvalidStateTransitionException always thrown as SUSPENDED cannot be an initial state
     */
    @Override
    public void validateInitial() {
        throw new InvalidStateTransitionException("SUSPENDED cannot be an initial branch status");
    }
}
