package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class CancelledState implements VehicleServiceState {

    @Override
    public void transitionTo(Vehicleservice service, Vehicleservicestatus newStatus) {

        throw new InvalidStateTransitionException(
                "Cancelled service cannot transition to another state"
        );
    }
}
