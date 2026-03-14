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
public class AllocationStateTransitionHandler {

    private final IncidentVehicleAllocationStatusFactory allocationStateFactory;
    private final IncidentVehicleAllocationRepository allocationRepository;

    public void transitionTo(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus newStatus) {

        IncidentVehicleAllocationStatus currentStatus = allocation.getIncidentvehicleallocationstatus();
        log.info("Transitioning allocation {} from {} to {}",
                allocation.getId(), currentStatus, newStatus);

        // Exit behavior (optional)
        executeOnExit(allocation, currentStatus);

        // Validate and perform transition
        IncidentVehicleAllocationState currentState =
                allocationStateFactory.getState(currentStatus.getName());
        currentState.transitionTo(allocation, newStatus);

        // Entry behavior (optional)
        executeOnEnter(allocation, newStatus);

        allocationRepository.save(allocation);
    }

    private void executeOnExit(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus status) {
        log.debug("Exiting {} state for allocation {}", status, allocation.getId());
    }

    private void executeOnEnter(IncidentVehicleAllocation allocation, IncidentVehicleAllocationStatus status) {
        log.debug("Entering {} state for allocation {}", status, allocation.getId());
        if (status.getName().equalsIgnoreCase("ASSIGNED")) {
            allocation.setDoassigned(LocalDateTime.now());
        } else if (status.getName().equalsIgnoreCase("RELEASED")) {
            allocation.setDoreleased(LocalDateTime.now());
        }
    }
}
