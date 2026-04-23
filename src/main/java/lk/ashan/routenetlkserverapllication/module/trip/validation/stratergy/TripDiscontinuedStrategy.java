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
public class TripDiscontinuedStrategy {

    private final TripExecutionService tripExecutionService;
    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;


    public void discontinueTrip(Trip trip) {
        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        // 1. Check for Active Journeys (The Hard Block)
        boolean hasLiveTrips = tripExecutions.stream()
                .anyMatch(t -> t.getTripexecutionstatus().getName().equals("IN PROGRESS"));

        if (hasLiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: A vehicle is currently performing a journey for this route."
            );
        }

        // 2. Check for Financial Integrity (The Audit Block)
        // If a trip is COMPLETED but not yet SETTLED, we can't discontinue the route master
        boolean hasUnsettledAccounts = tripExecutions.stream()
                .anyMatch(t -> t.getTripexecutionstatus().getName().equals("COMPLETED"));

        if (hasUnsettledAccounts) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: There are completed trips awaiting fare collection/settlement."
            );
        }

        // 3. Perform Cleanup (Future-proofing)
        // Remove or Cancel all future/assigned trips that haven't happened
//        List<String> removableStatuses = List.of("SCHEDULED", "ASSIGNED", "READY");
//        tripExecutions.stream()
//                .filter(t -> removableStatuses.contains(t.getTripexecutionstatus().getName()))
//                .forEach(t -> tripExecutionService.deleteOrCancelExecution(t.getId()));

        // 4. Final Transition
        Tripstatus discontinuedStatus = tripStatusService.getByName("Discontinued");
        tripStateTransitionHandler.transitionTo(trip, discontinuedStatus);
    }
}
