package lk.ashan.routenetlkserverapllication.module.vehicleservice.state;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleService;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceStateTransitionHandler {

    private final VehicleServiceStateFactory stateFactory;

    public void transitionTo(VehicleService service, VehicleServiceStatus newStatus) {
        String currentStatus = service.getVehicleservicestatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning service {} from {} to {}", service.getId(), currentStatus, targetStatus);

        executeOnExit(service, currentStatus);

        VehicleServiceState currentState = stateFactory.getState(currentStatus);
        currentState.transitionTo(service, newStatus);

        executeOnEnter(service, targetStatus);
    }

    private void executeOnExit(VehicleService service, String statusName) {
        String normalized = statusName.trim().toUpperCase().replace(" ", "_");
        switch (normalized) {
            case "PENDING" -> log.debug("Exiting PENDING state for service {}", service.getId());
            case "SCHEDULED" -> log.debug("Exiting SCHEDULED state for service {}", service.getId());
            case "IN_PROGRESS" -> log.debug("Exiting IN_PROGRESS state for service {}", service.getId());
            case "ON_HOLD_PARTS" -> log.debug("Exiting ON_HOLD_PARTS state for service {}", service.getId());
        }
    }

    private void executeOnEnter(VehicleService service, String statusName) {
        String normalized = statusName.trim().toUpperCase().replace(" ", "_");
        switch (normalized) {
            case "SCHEDULED" -> log.info("Service {} scheduled", service.getId());
            case "IN_PROGRESS" -> log.info("Service {} started", service.getId());
            case "ON_HOLD_PARTS" -> log.info("Service {} placed on hold for inventory parts", service.getId());
            case "COMPLETED" -> log.info("Service {} completed", service.getId());
            case "CANCELLED" -> log.info("Service {} cancelled", service.getId());
        }
    }
}
