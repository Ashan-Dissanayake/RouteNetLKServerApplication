package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchSuspendedState implements BranchState {

    private static final List<String> ALLOWED = List.of("ACTIVE", "CLOSED");

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

    @Override
    public void validateInitial() {
        throw new InvalidStateTransitionException("SUSPENDED cannot be an initial branch status");
    }
}
