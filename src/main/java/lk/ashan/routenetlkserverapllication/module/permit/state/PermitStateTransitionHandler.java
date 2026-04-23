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


    public void transitionTo(Permite permite, PermiteStatus targetStatus) {
        String currentStatus = permite.getPermitestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning permit {} from {} to {}", permite.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(permite, currentStatus);

        // Validate & transition
        PermitState currentState = permitStateFactory.getState(currentStatus);
        currentState.transitionTo(permite, targetStatus);

        // Entry behavior
        executeOnEnter(permite, target);
    }

    private void executeOnExit(Permite permite, String statusName) {

    }

    private void executeOnEnter(Permite permite, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        /* No entry behavior for other states */
        if (normalized.equals("TRANSFERRED")) {
            onEnterTransferred(permite);
        }
    }

    private void onEnterTransferred(Permite permite) {
        Vehicle vehicle =
                permite.getVehicle();

        VehicleStatus available =
                vehicleStatusService
                        .getByName("Available");

        vehicleStateTransitionHandler
                .transitionTo(vehicle, available);
    }
}
