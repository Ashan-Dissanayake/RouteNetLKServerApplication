package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class TripExecutionCompletedState implements TripExecutionState {

    @Override
    public void transitionTo(
            TripExecution trip,
            TripExecutionStatus nextStatus
    ) {
        throw new InvalidStateTransitionException(
                "Completed trips cannot be changed"
        );
    }
}
