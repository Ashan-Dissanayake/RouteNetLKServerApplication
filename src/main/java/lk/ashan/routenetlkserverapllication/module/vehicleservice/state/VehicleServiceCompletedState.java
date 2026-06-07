package lk.ashan.routenetlkserverapllication.module.vehicleservice.state;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class VehicleServiceCompletedState implements VehicleServiceState {
    @Override
    public void transitionTo(VehicleService service, VehicleServiceStatus newStatus) {
        throw new InvalidStateTransitionException("Service already completed. No further transitions allowed.");
    }
}
