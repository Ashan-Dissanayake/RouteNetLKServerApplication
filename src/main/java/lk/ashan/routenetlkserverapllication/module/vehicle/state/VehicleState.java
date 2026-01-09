package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;

public interface VehicleState {
    void transitionTo(Vehicle vehicle, Vehiclestatus newStatus);
}
