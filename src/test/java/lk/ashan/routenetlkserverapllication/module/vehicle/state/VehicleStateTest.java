package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.Vehiclestatus;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VehicleStateTest {

    @Test
    void availableState_shouldAllow_inService() {
        AvailableState state = new AvailableState();
        Vehicle vehicle = new Vehicle();
        Vehiclestatus newStatus = new Vehiclestatus();
        newStatus.setName("IN SERVICE");

        assertDoesNotThrow(() -> state.transitionTo(vehicle, newStatus));
    }

    @Test
    void availableState_shouldThrow_decommissioned() {
        AvailableState state = new AvailableState();
        Vehicle vehicle = new Vehicle();
        Vehiclestatus newStatus = new Vehiclestatus();
        newStatus.setName("DECOMMISSIONED");

        assertThrows(InvalidStatusTransitionException.class, () -> state.transitionTo(vehicle, newStatus));
    }

    @Test
    void inServiceState_shouldAllow_available() {
        InServiceState state = new InServiceState();
        Vehicle vehicle = new Vehicle();
        Vehiclestatus newStatus = new Vehiclestatus();
        newStatus.setName("AVAILABLE");

        assertDoesNotThrow(() -> state.transitionTo(vehicle, newStatus));
    }

}
