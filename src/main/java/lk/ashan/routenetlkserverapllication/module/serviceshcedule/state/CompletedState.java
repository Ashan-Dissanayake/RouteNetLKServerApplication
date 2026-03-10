package lk.ashan.routenetlkserverapllication.module.serviceshcedule.state;

import lk.ashan.routenetlkserverapllication.module.serviceshcedule.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.serviceshcedule.model.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class CompletedState implements VehicleServiceState {

    @Override
    public void transitionTo(Vehicleservice service, Vehicleservicestatus newStatus) {

        throw new InvalidStateTransitionException(
                "Service already completed. No further transitions allowed."
        );
    }
}
