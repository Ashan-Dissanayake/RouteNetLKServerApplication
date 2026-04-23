package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TripActivationStrategy {

    private final TripStatusService tripStatusService;
    private final TripStateTransitionHandler tripStateTransitionHandler;

    public void activateTrip(Trip trip){
     String currentStatus =   trip.getPermite().getVehicle().getVehiclestatus().getName().toUpperCase();
     List<String> allowedStatuses = List.of("AVAILABLE","ALLOCATED");
        if (!allowedStatuses.contains(currentStatus)) {
            throw new BusinessRuleViolationException(
                    String.format("Cannot activate trip. Vehicle is currently in '%s' status.", currentStatus)
            );
        }
       Tripstatus activateStatus = tripStatusService.getByName("Active");
       tripStateTransitionHandler.transitionTo(trip, activateStatus);
    }

}
