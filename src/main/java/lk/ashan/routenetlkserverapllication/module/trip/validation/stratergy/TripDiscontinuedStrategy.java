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
 * Strategy for discontinuing a trip. This class handles the validation and state transition
 * required to mark a trip as discontinued.
 */
@Component
@RequiredArgsConstructor
public class TripDiscontinuedStrategy {

    private final TripExecutionService tripExecutionService;
    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;

    /**
     * Discontinues a trip after performing necessary validations.
     * Validations include checking for live trips and unsettled accounts.
     *
     * @param trip The trip to be discontinued.
     * @throws BusinessRuleViolationException if there are live trips in progress
     *                                        or completed trips with unsettled accounts.
     */
    public void discontinueTrip(Trip trip) {
        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        boolean hasLiveTrips = tripExecutions.stream()
                .anyMatch(t -> t.getTripexecutionstatus().getName().equalsIgnoreCase("in progress"));

        if (hasLiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: A vehicle is currently performing a journey for this route."
            );
        }

        boolean hasUnsettledAccounts = tripExecutions.stream()
                .anyMatch(t -> t.getTripexecutionstatus().getName().equalsIgnoreCase("Completed"));

        if (hasUnsettledAccounts) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: There are completed trips awaiting fare collection/settlement."
            );
        }

        Tripstatus discontinuedStatus = tripStatusService.getByName("Discontinued");
        tripStateTransitionHandler.transitionTo(trip, discontinuedStatus);
    }
}
