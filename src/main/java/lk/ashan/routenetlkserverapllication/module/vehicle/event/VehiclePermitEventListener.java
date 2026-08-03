package lk.ashan.routenetlkserverapllication.module.vehicle.event;

import lk.ashan.routenetlkserverapllication.module.permit.event.PermitTransferredEvent;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleStatusService;
import lk.ashan.routenetlkserverapllication.module.vehicle.state.VehicleStateTransitionHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehiclePermitEventListener {

    private final VehicleStatusService vehicleStatusService;
    private final VehicleStateTransitionHandler vehicleStateTransitionHandler;

    @EventListener
    public void handlePermitTransferred(PermitTransferredEvent event) {
        if (event.vehicle() == null) {
            log.warn("PermitTransferredEvent received for permit ID {}, but vehicle is null", event.permit().getId());
            return;
        }

        log.info("VehiclePermitEventListener: Handling PermitTransferredEvent for permit ID {} and vehicle ID {}",
                event.permit().getId(), event.vehicle().getId());

        Vehicle vehicle = event.vehicle();
        VehicleStatus availableStatus = vehicleStatusService.getByName("Available");

        vehicleStateTransitionHandler.transitionTo(vehicle, availableStatus);
    }
}
