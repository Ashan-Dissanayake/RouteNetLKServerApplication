package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.entity.IncidentVehicleAllocationStatus;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class IncidentVehicleAllocationValidator {

    private final IncidentVehicleAllocationRepository allocationRepository;

    public void validateCreate(
            Incident incident,
            Vehicle vehicle
    ) {
        validateIncidentState(incident);
        validateVehicleAvailability(vehicle);
        validateDuplicateAllocation(
                incident.getId(),
                vehicle.getId()
        );
        validateIncidentAllocationLimit(
                incident.getId()
        );
    }

    public void validateInitialStatus(
            IncidentVehicleAllocationStatus status
    ) {
        if (!"ASSIGNED".equalsIgnoreCase(status.getName())) {
            throw new BusinessRuleViolationException(
                    "New vehicle allocations must start with Assigned status"
            );
        }
    }

    public void validateStatusTransition(
            IncidentVehicleAllocationStatus currentStatus,
            IncidentVehicleAllocationStatus targetStatus
    ) {
        String current = normalize(currentStatus.getName());
        String target = normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "ASSIGNED" ->
                    target.equals("IN PROGRESS")
                            || target.equals("CANCELLED");

            case "IN PROGRESS" ->
                    target.equals("RELEASED")
                            || target.equals("CANCELLED");

            case "RELEASED",
                 "CANCELLED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid allocation status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    private void validateIncidentState(
            Incident incident
    ) {
        String status =
                incident.getIncidentstatus().getName();

        if ("Closed".equalsIgnoreCase(status)
                || "Resolved".equalsIgnoreCase(status)) {

            throw new BusinessRuleViolationException(
                    "Cannot allocate vehicle. The Incident is already "
                            + status
            );
        }
    }

    private void validateVehicleAvailability(
            Vehicle vehicle
    ) {
        String vehicleStatus =
                vehicle.getVehiclestatus().getName();

        if (!"Available".equalsIgnoreCase(vehicleStatus)) {

            throw new BusinessRuleViolationException(
                    "Vehicle is "
                            + vehicleStatus
                            + " and cannot be used for relief."
            );
        }

        boolean alreadyAllocated =
                allocationRepository
                        .existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                vehicle.getId(),
                                List.of("Assigned", "In Progress")
                        );

        if (alreadyAllocated) {

            throw new BusinessRuleViolationException(
                    "This bus is already assigned to another emergency."
            );
        }
    }

    private void validateDuplicateAllocation(
            Integer incidentId,
            Integer vehicleId
    ) {
        boolean exists =
                allocationRepository
                        .existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                                incidentId,
                                vehicleId,
                                List.of("Assigned", "In Progress")
                        );

        if (exists) {

            throw new BusinessRuleViolationException(
                    "Duplicate active allocation for this incident and vehicle"
            );
        }
    }

    private void validateIncidentAllocationLimit(
            Integer incidentId
    ) {
        boolean hasActive =
                allocationRepository
                        .existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                                incidentId,
                                List.of("Assigned", "In Progress")
                        );

        if (hasActive) {

            throw new BusinessRuleViolationException(
                    "This incident already has an active relief bus assigned."
            );
        }
    }

    private String normalize(String status) {
        return status.trim().toUpperCase();
    }
}