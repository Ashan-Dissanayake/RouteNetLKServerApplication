package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Represents the CLOSED state of a branch. In this state, no transitions are allowed.
 * This class implements the BranchState interface.
 */
@Component
public class BranchClosedState implements BranchState {

    private static final List<String> ALLOWED = List.of(); // No transitions allowed

    /**
     * Throws an exception as no transitions are allowed from the CLOSED state.
     *
     * @param branch       the branch entity attempting the transition
     * @param targetStatus the target status for the transition
     * @throws InvalidStateTransitionException always thrown as transitions are not allowed
     */
    @Override
    public void transitionTo(Branch branch, BranchStatus targetStatus) {
        throw new InvalidStateTransitionException("No transitions allowed from CLOSED state");
    }

    /**
     * Validates the initial state of the branch. Throws an exception as CLOSED cannot be an initial state.
     *
     * @throws InvalidStateTransitionException always thrown as CLOSED cannot be an initial state
     */
    @Override
    public void validateInitial() {
        throw new InvalidStateTransitionException("CLOSED cannot be an initial branch status");
    }
}
