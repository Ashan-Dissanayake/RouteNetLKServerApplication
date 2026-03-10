package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.state;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservice;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.Vehicleservicestatus;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.repository.VehicleServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceStateTransitionHandler {

    private final VehicleServiceStateFactory stateFactory;
    private final VehicleServiceRepository vehicleServiceRepository;

    public void transitionTo(Vehicleservice service, Vehicleservicestatus newStatus) {

        String currentStatus = service.getVehicleservicestatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning service {} from {} to {}",
                service.getId(), currentStatus, targetStatus);

        executeOnExit(service, currentStatus);

        VehicleServiceState currentState =
                stateFactory.getState(currentStatus);

        currentState.transitionTo(service, newStatus);

        executeOnEnter(service, targetStatus);

        vehicleServiceRepository.save(service);
    }

    private void executeOnExit(Vehicleservice service, String statusName) {

        switch (statusName.toUpperCase()) {

            case "CREATED" ->
                    log.debug("Exiting CREATED state for service {}", service.getId());

            case "SCHEDULED" ->
                    log.debug("Exiting SCHEDULED state for service {}", service.getId());

            case "IN_PROGRESS" ->
                    log.debug("Exiting IN_PROGRESS state for service {}", service.getId());
        }
    }

    private void executeOnEnter(Vehicleservice service, String statusName) {

        switch (statusName.toUpperCase()) {

            case "SCHEDULED" ->
                    log.info("Service {} scheduled", service.getId());

            case "IN_PROGRESS" ->
                    log.info("Service {} started", service.getId());

            case "COMPLETED" ->
                    log.info("Service {} completed", service.getId());

            case "CANCELLED" ->
                    log.info("Service {} cancelled", service.getId());
        }
    }
}
