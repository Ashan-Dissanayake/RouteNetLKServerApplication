package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TripExecutionScheduledState implements TripExecutionState {
    private static final List<String> ALLOWED = List.of("CHECKED IN", "CANCELLED");

    @Override
    public void transitionTo(TripExecution tripExecution, TripExecutionStatus nextStatus) {
        if ("SCHEDULED".equalsIgnoreCase(nextStatus.getName())) return;
        String target = nextStatus.getName().toUpperCase();
        if (!ALLOWED.contains(target)) {
            throw new InvalidStateTransitionException("Scheduled trips can only move to Checked-In or Cancelled");
        }
        tripExecution.setTripexecutionstatus(nextStatus);
    }

}
