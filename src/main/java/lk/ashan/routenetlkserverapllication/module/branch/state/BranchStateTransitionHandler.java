package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.module.branch.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BranchStateTransitionHandler {

    private final BranchStateFactory branchStateFactory;

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

    private void executeOnExit(Branch branch, String statusName) {
        log.debug("Exiting {} state for branch {}", statusName, branch.getId());
    }

    private void executeOnEnter(Branch branch, String statusName) {
        log.info("Entering {} state for branch {}", statusName, branch.getId());
    }
}
