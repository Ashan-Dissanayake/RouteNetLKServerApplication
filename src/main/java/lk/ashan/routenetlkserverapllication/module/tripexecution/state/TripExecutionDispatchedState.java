package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TripExecutionDispatchedState implements TripExecutionState {
    private static final List<String> ALLOWED = List.of("ARRIVED", "BREAKDOWN");

    @Override
    public void transitionTo(TripExecution tripExecution, TripExecutionStatus nextStatus) {
        if ("DISPATCHED".equalsIgnoreCase(nextStatus.getName())) return;
        String target = nextStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException("Dispatched trips must move to In-Progress or handle a Breakdown");
        }

        tripExecution.setTripexecutionstatus(nextStatus);
    }
}
