package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Strategy for activating a trip. Ensures that the vehicle associated with the trip
 * is in an allowed status before transitioning the trip to the "Active" state.
 */
@Component
@RequiredArgsConstructor
public class TripActivationStrategy {

    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;

    /**
     * Activates the given trip by transitioning its state to "Active".
     * Validates that the vehicle associated with the trip is in an allowed status
     * before performing the state transition.
     *
     * @param trip the trip to be activated
     * @throws BusinessRuleViolationException if the vehicle's current status is not allowed
     */
    public void activateTrip(Trip trip) {
        String currentStatus = trip.getPermite().getVehicle().getVehiclestatus().getName().toUpperCase();
        List<String> allowedStatuses = List.of("AVAILABLE", "ALLOCATED");
        if (!allowedStatuses.contains(currentStatus)) {
            throw new BusinessRuleViolationException(
                    String.format("Cannot activate trip. Vehicle is currently in '%s' status.", currentStatus)
            );
        }
        Tripstatus activateStatus = tripStatusService.getByName("Active");
        tripStateTransitionHandler.transitionTo(trip, activateStatus);
    }

}
