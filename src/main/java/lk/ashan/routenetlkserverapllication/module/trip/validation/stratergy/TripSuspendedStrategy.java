package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Strategy for suspending a trip. This class handles the validation and state transition
 * required to suspend a trip.
 */
@Component
@RequiredArgsConstructor
public class TripSuspendedStrategy {

    private final TripExecutionService tripExecutionService;
    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;

    /**
     * Suspends the given trip if there are no active trips in progress.
     *
     * @param trip the trip to be suspended
     * @throws BusinessRuleViolationException if there are trips currently 'IN PROGRESS'
     */
    public void suspendTrip(Trip trip) {
        List<String> hardBlockers = List.of("IN PROGRESS");

        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        boolean hasActiveTrips = tripExecutions.stream()
                .anyMatch(t -> hardBlockers.contains(t.getTripexecutionstatus().getName()));

        if (hasActiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot suspend the Master Schedule because there are trips currently 'IN PROGRESS' on the road."
            );
        }

        Tripstatus suspendStatus = tripStatusService.getByName("Suspended");
        tripStateTransitionHandler.transitionTo(trip, suspendStatus);
    }
}
