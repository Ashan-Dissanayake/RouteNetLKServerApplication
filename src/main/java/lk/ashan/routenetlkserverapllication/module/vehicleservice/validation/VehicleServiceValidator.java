package lk.ashan.routenetlkserverapllication.module.vehicleservice.validation;

import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.entity.VehicleServiceStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VehicleServiceValidator {

    private final VehicleRepository vehicleRepository;
    private final IncidentRepository incidentRepository;

    public void validateCreate(
            VehicleServiceCreateRequestDto request
    ) {
        validatePartQuantities(
                request.getVehicleserviceparts()
        );

        validateBreakdownRepairIncident(
                request
        );

        validateVehicleAvailability(
                request.getVehicle().getId()
        );
    }

    public void validateStatusTransition(
            VehicleServiceStatus currentStatus,
            VehicleServiceStatus targetStatus
    ) {
        String current =
                normalize(currentStatus.getName());

        String target =
                normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "PENDING" ->
                    target.equals("SCHEDULED")
                            || target.equals("IN_PROGRESS")
                            || target.equals("CANCELLED");

            case "SCHEDULED" ->
                    target.equals("IN_PROGRESS")
                            || target.equals("CANCELLED");

            case "IN_PROGRESS" ->
                    target.equals("COMPLETED")
                            || target.equals("ON_HOLD_PARTS")
                            || target.equals("CANCELLED");

            case "ON_HOLD_PARTS" ->
                    target.equals("IN_PROGRESS")
                            || target.equals("CANCELLED");

            case "COMPLETED",
                 "CANCELLED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new InvalidStateTransitionException(
                    "Invalid vehicle service status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    private void validatePartQuantities(
            List<VehicleServicePartDto> parts
    ) {
        if (parts == null) {
            return;
        }

        for (VehicleServicePartDto part : parts) {

            if (part.getQuantity() == null
                    || part.getQuantity()
                    .compareTo(BigDecimal.ZERO) <= 0) {

                throw new BusinessRuleViolationException(
                        "Requested part quantity must be greater than zero"
                );
            }
        }
    }

    private void validateBreakdownRepairIncident(
            VehicleServiceCreateRequestDto request
    ) {
        String serviceType =
                request.getVehicleservicetype()
                        .getName();

        if (!"BREAKDOWN_REPAIR".equalsIgnoreCase(
                serviceType
        )) {
            return;
        }

        if (request.getIncident() == null
                || request.getIncident().getId() == null) {

            throw new BusinessRuleViolationException(
                    "An active incident ID must be attached for breakdown repairs"
            );
        }

        incidentRepository.findById(
                request.getIncident().getId()
        ).orElseThrow(() ->
                new BusinessRuleViolationException(
                        "Attached incident target not found"
                )
        );
    }

    private void validateVehicleAvailability(
            Integer vehicleId
    ) {
        Vehicle vehicle =
                vehicleRepository.findById(vehicleId)
                        .orElseThrow(() ->
                                new BusinessRuleViolationException(
                                        "Vehicle target not found"
                                )
                        );

        String status =
                vehicle.getVehiclestatus()
                        .getName();

        if ("UNDER_MAINTENANCE".equalsIgnoreCase(
                status
        )) {
            throw new BusinessRuleViolationException(
                    "This vehicle is already booked into a maintenance loop"
            );
        }
    }

    private String normalize(String value) {
        return value.trim()
                .toUpperCase()
                .replace(" ", "_");
    }
}