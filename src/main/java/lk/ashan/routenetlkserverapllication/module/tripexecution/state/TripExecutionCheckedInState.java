package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripExecutionCheckedInState implements TripExecutionState {

    private static final List<String> ALLOWED = List.of("DISPATCHED", "CANCELLED");

    @Override
    public void transitionTo(TripExecution tripExecution, TripExecutionStatus nextStatus) {
        if ("CHECKED IN".equalsIgnoreCase(nextStatus.getName())) return;
        String target = nextStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException("CHECKED IN trips can only move to DISPATCHED or CANCELLED");
        }

        tripExecution.setTripexecutionstatus(nextStatus);
    }



}
