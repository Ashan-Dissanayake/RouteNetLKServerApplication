package lk.ashan.routenetlkserverapllication.module.vehicleservice.state;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleServicePendingState implements VehicleServiceState {

    private static final List<String> ALLOWED =
            List.of("SCHEDULED", "IN_PROGRESS", "CANCELLED");

    @Override
    public void transitionTo(VehicleService service, VehicleServiceStatus newStatus) {
        if (!ALLOWED.contains(newStatus.getName().toUpperCase())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from PENDING to " + newStatus.getName()
            );
        }
        service.setVehicleservicestatus(newStatus);
    }

    @Override
    public void validateInitial() { }
}
