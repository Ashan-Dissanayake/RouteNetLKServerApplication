package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripExecutionArrivedState implements TripExecutionState {

    private static final List<String> ALLOWED = List.of("COMPLETED");

    @Override
    public void transitionTo(TripExecution trip, TripExecutionStatus nextStatus) {

        if ("ARRIVED".equalsIgnoreCase(nextStatus.getName())) return;

        String target = nextStatus.getName().toUpperCase();

        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException(
                    "Arrived trips can only move to Completed"
            );
        }

        trip.setTripexecutionstatus(nextStatus);
    }
}
