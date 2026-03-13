package lk.ashan.routenetlkserverapllication.module.branch.state;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.BranchStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchClosedState implements BranchState {

    private static final List<String> ALLOWED = List.of(); // No transitions allowed

    @Override
    public void transitionTo(Branch branch, BranchStatus targetStatus) {
        throw new InvalidStateTransitionException("No transitions allowed from CLOSED state");
    }

    @Override
    public void validateInitial() {
        throw new InvalidStateTransitionException("CLOSED cannot be an initial branch status");
    }
}
