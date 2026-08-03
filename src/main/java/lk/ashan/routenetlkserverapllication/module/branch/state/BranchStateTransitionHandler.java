package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles state transitions for branches. This class is responsible for managing
 * the transition process between different states of a branch, including executing
 * entry and exit behaviors.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BranchStateTransitionHandler {

    private final BranchStateFactory branchStateFactory;

    /**
     * Transitions a branch from its current state to a target state.
     *
     * @param branch       the branch entity to transition
     * @param targetStatus the target status to transition to
     * @throws IllegalStateException if the transition is invalid
     */
    public void transitionTo(Branch branch, BranchStatus targetStatus) {
        String currentStatus = branch.getBranchstatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning branch {} from {} to {}", branch.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(branch, currentStatus);

        // Validate & transition
        BranchState currentState = branchStateFactory.getState(currentStatus);
        currentState.transitionTo(branch, targetStatus);

        // Entry behavior
        executeOnEnter(branch, target);
    }

    /**
     * Executes the exit behavior for the current state of the branch.
     *
     * @param branch     the branch entity
     * @param statusName the name of the current state being exited
     */
    private void executeOnExit(Branch branch, String statusName) {
        log.debug("Exiting {} state for branch {}", statusName, branch.getId());
    }

    /**
     * Executes the entry behavior for the target state of the branch.
     *
     * @param branch     the branch entity
     * @param statusName the name of the target state being entered
     */
    private void executeOnEnter(Branch branch, String statusName) {
        log.info("Entering {} state for branch {}", statusName, branch.getId());
    }
}
