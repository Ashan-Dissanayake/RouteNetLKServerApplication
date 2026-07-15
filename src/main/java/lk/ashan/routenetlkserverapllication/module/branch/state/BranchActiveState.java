package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the active state of a branch. This state allows transitions
 * to specific states such as SUSPENDED or CLOSED.
 */
@Component
public class BranchActiveState implements BranchState {

    private static final List<String> ALLOWED = List.of("SUSPENDED", "CLOSED");

    /**
     * Transitions the branch to the specified target status if the transition is valid.
     *
     * @param branch       the branch whose state is being transitioned
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
                    "Invalid transition from ACTIVE to " + targetStatus.getName()
            );
        }
        branch.setBranchstatus(targetStatus);
    }

    /**
     * Validates the initial state of the branch. This method is a no-op for the active state.
     */
    @Override
    public void validateInitial() {}
}
