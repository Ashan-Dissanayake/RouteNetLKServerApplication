package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VehicleServiceInProgressState implements VehicleServiceState {

    private static final List<String> ALLOWED =
            List.of("COMPLETED", "CANCELLED");

    @Override
    public void transitionTo(VehicleService service, VehicleServiceStatus newStatus) {

        if (!ALLOWED.contains(newStatus.getName())) {
            throw new InvalidStateTransitionException(
                    "Invalid transition from IN_PROGRESS to " + newStatus.getName()
            );
        }
        service.setVehicleservicestatus(newStatus);
    }
}
