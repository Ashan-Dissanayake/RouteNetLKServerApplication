package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.entity.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class VehicleServiceCompletedState implements VehicleServiceState {

    @Override
    public void transitionTo(Vehicleservice service, Vehicleservicestatus newStatus) {

        throw new InvalidStateTransitionException(
                "Service already completed. No further transitions allowed."
        );
    }
}
