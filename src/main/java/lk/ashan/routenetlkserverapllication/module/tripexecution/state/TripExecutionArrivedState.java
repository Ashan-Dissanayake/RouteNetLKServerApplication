package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;

import java.util.List;

public class TripExecutionArrivedState implements TripExecutionState{
    private static final List<String> ALLOWED = List.of("ARRIVED", "BREAKDOWN");
    @Override
    public void transitionTo(TripExecution trip, TripExecutionStatus nextStatus) {
        if ("ARRIVED".equalsIgnoreCase(nextStatus.getName())) return;
        String target = nextStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException("Arrived trips must move to Completed");
        }
        trip.setTripexecutionstatus(nextStatus);
    }
}
