package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchActiveState implements BranchState {

    private static final List<String> ALLOWED = List.of("SUSPENDED", "CLOSED");

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

    @Override
    public void validateInitial() {}
}
