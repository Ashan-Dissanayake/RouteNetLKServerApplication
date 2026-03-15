package lk.ashan.routenetlkserverapllication.module.vehicle.state;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleStateTransitionHandler {

    private final VehicleStateFactory vehicleStateFactory;

    public void transitionTo(Vehicle vehicle, VehicleStatus targetStatus) {
        String currentStatus = vehicle.getVehiclestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning vehicle {} from {} to {}", vehicle.getId(), currentStatus, target);

        executeOnExit(vehicle, currentStatus);

        VehicleState currentState = vehicleStateFactory.getState(currentStatus);
        currentState.transitionTo(vehicle, targetStatus);

        executeOnEnter(vehicle, target);
    }

    private void executeOnExit(Vehicle vehicle, String statusName) {
        log.debug("Exiting {} state for vehicle {}", statusName, vehicle.getId());
    }

    private void executeOnEnter(Vehicle vehicle, String statusName) {
        log.info("Entering {} state for vehicle {}", statusName, vehicle.getId());
    }
}
