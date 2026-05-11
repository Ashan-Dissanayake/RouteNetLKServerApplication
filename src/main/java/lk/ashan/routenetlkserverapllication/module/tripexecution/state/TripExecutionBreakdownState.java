package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripExecutionBreakdownState implements TripExecutionState{
    @Override
    public void transitionTo(TripExecution trip, TripExecutionStatus nextStatus) {
        throw new InvalidStateTransitionException(
                "No Transition Allowed from Breakdown"
        );
    }
}
