package lk.ashan.routenetlkserverapllication.module.permit.state;


import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleStatusService;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateTransitionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermitStateTransitionHandler {

    private final PermitStateFactory permitStateFactory;
    private final VehicleStatusService vehicleStatusService;
    private final VehicleStateTransitionHandler vehicleStateTransitionHandler;


    public void transitionTo(Permite permit, PermiteStatus targetStatus) {
        String currentStatus = permit.getPermitestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning permit {} from {} to {}", permit.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(permit, currentStatus);

        // Validate & transition
        PermitState currentState = permitStateFactory.getState(currentStatus);
        currentState.transitionTo(permit, targetStatus);

        // Entry behavior
        executeOnEnter(permit, target);
    }

    private void executeOnExit(Permite permit, String statusName) {

    }

    private void executeOnEnter(Permite permit, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        /* No entry behavior for other states */
        if (normalized.equals("TRANSFERRED")) {
            onEnterTransferred(permit);
        }
    }

    private void onEnterTransferred(Permite permit) {
        Vehicle vehicle =
                permit.getVehicle();

        VehicleStatus available =
                vehicleStatusService
                        .getByName("Available");

        vehicleStateTransitionHandler
                .transitionTo(vehicle, available);
    }
}
