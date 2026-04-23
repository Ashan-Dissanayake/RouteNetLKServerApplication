package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.tripexecution.TripExecutionService;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripSuspendedStrategy {

    private final TripExecutionService tripExecutionService;
    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;


    public void suspendTrip(Trip trip) {
        // 1. Define the Hard Blockers
        List<String> hardBlockers = List.of("IN PROGRESS");

        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        // 2. Check if any active execution is a hard blocker
        boolean hasActiveTrips = tripExecutions.stream()
                .anyMatch(t -> hardBlockers.contains(t.getTripexecutionstatus().getName()));

        if (hasActiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot suspend the Master Schedule because there are trips currently 'IN PROGRESS' on the road."
            );
        }

//         3. Handle Soft Blockers (Optional: Auto-cancel trips that haven't started)
//        List<String> softBlockers = List.of("ASSIGNED", "READY");
//         tripExecutions.stream()
//                       .filter(t -> softBlockers.contains(t.getTripexecutionstatus().getName()))
//                .forEach(t -> tripExecutionService.cancelExecution(t.getId(), "Master trip suspended"));

        // 4. Update Master Status
        Tripstatus suspendStatus = tripStatusService.getByName("Suspended");
        tripStateTransitionHandler.transitionTo(trip, suspendStatus);
    }
}
