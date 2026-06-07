package lk.ashan.routenetlkserverapllication.module.vehicleservice.state;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleServiceScheduledState implements VehicleServiceState {

    private static final List<String> ALLOWED =
            List.of("IN_PROGRESS", "CANCELLED");

    @Override
    public void transitionTo(VehicleService service, VehicleServiceStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from SCHEDULED to " + newStatus.getName()
            );
        }
        service.setVehicleservicestatus(newStatus);
    }
}
