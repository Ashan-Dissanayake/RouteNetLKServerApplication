package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
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
    private final TripRepository tripRepository;
    private final TripStateTransitionHandler tripStateTransitionHandler;


    /**
         * Activates a trip by ensuring the associated vehicle is in an allowed status
         * and no other active trip exists for the same route, origin terminal, and departure time.
         *
         * @param trip the trip to be activated
         * @throws BusinessRuleViolationException if the vehicle is not in an allowed status
         *                                        or if an active trip already exists for the same route,
         *                                        origin terminal, and departure time
         */
        public void activateTrip(Trip trip) {

            String currentStatus = trip.getPermite().getVehicle().getVehiclestatus().getName().toUpperCase();

            List<String> allowedStatuses = List.of("AVAILABLE", "ALLOCATED");

            if (!allowedStatuses.contains(currentStatus)) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Cannot activate trip. Vehicle is currently in '%s' status.",
                                currentStatus
                        )
                );
            }

            boolean exists = tripRepository.existsActiveTrip(
                    trip.getPermite().getRoute().getId(),
                    trip.getOriginterminal().getId(),
                    trip.getTodepature(),
                    trip.getId()
            );

            if (exists) {
                throw new BusinessRuleViolationException(
                        "An active trip already exists for the same route, origin terminal and departure time."
                );
            }

            Tripstatus activateStatus = tripStatusService.getByName("Active");
            tripStateTransitionHandler.transitionTo(trip, activateStatus);
        }

}
