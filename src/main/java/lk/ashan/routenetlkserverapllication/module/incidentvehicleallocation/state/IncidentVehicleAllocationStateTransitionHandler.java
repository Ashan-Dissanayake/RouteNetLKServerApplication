package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.state;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocation;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class IncidentVehicleAllocationStateTransitionHandler {

    private final IncidentVehicleAllocationStatusFactory allocationStateFactory;
    private final IncidentVehicleAllocationRepository allocationRepository;

    public void transitionTo(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus newStatus) {
        IncidentVehicleAllocationStatus currentStatus = allocation.getIncidentvehicleallocationstatus();
        log.info("Transitioning allocation {} from {} to {}",
                allocation.getId(), currentStatus.getName(), newStatus.getName());

        executeOnExit(allocation, currentStatus);

        IncidentVehicleAllocationState currentState =
                allocationStateFactory.getState(currentStatus.getName());

        currentState.transitionTo(allocation, newStatus);

        executeOnEnter(allocation, newStatus);

        allocationRepository.save(allocation);
    }

    private void executeOnExit(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus status) {
        log.debug("Exiting {} state for allocation {}", status.getName(), allocation.getId());
    }

    private void executeOnEnter(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus status) {
        log.debug("Entering {} state for allocation {}", status.getName(), allocation.getId());
        String name = status.getName().toUpperCase();

        if (name.equals("ASSIGNED")) {
            allocation.setDoassigned(LocalDateTime.now());
        } else if (name.equals("RELEASED")) {
            allocation.setDoreleased(LocalDateTime.now());
        }
    }
}
