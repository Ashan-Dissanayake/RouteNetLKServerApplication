package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class IncidentValidator {

    /**
     * Validates incident creation rules.
     *
     * @param dto the incident creation request
     * @param tripExecution the related trip execution
     */
    public void validateCreate(
            IncidentCreateRequestDto dto,
            TripExecution tripExecution
    ) {
        validateIncidentTime(dto.getToreported(), tripExecution);
    }

    /**
     * Validates whether the reported incident time falls
     * within the trip execution duration.
     *
     * @param reportedTime the reported incident time
     * @param tripExecution the related trip execution
     */
    private void validateIncidentTime(
            LocalTime reportedTime,
            TripExecution tripExecution
    ) {
        LocalTime departure =
                tripExecution.getTrip().getTodepature();

        LocalTime arrival =
                tripExecution.getTrip().getToarrival();

        if (reportedTime.isBefore(departure)
                || reportedTime.isAfter(arrival)) {

            throw new BusinessRuleViolationException(
                    "Incident time must be within trip duration"
            );
        }
    }

    /**
     * Validates whether an incident can transition
     * from its current status to the target status.
     *
     * @param currentStatus the current incident status
     * @param targetStatus the requested target status
     */
    public void validateStatusTransition(
            IncidentStatus currentStatus,
            IncidentStatus targetStatus
    ) {

        String current = normalize(currentStatus.getName());
        String target = normalize(targetStatus.getName());

        // Same status transition is allowed.
        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "REPORTED" ->
                    target.equals("IN PROGRESS");

            case "IN PROGRESS" ->
                    target.equals("VEHICLE RECOVERY")
                            || target.equals("PENDING ALLOCATION")
                            || target.equals("RESOLVED");

            case "VEHICLE RECOVERY" ->
                    target.equals("PENDING ALLOCATION")
                            || target.equals("RESOLVED");

            case "PENDING ALLOCATION" ->
                    target.equals("RESOLVED");

            case "RESOLVED" ->
                    target.equals("CLOSED");

            case "CLOSED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid incident status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    /**
     * Validates whether the incident type allows
     * vehicle recovery.
     *
     * @param incident the incident
     */
    public void validateVehicleRecovery(Incident incident) {

        String incidentType =
                incident.getIncidenttype().getName();

        if (!"Accident".equalsIgnoreCase(incidentType)
                && !"Mechanical Breakdown".equalsIgnoreCase(incidentType)) {

            throw new BusinessRuleViolationException(
                    "Only MECHANICAL BREAKDOWN or ACCIDENT can be "
                            + "marked for VEHICLE RECOVERY"
            );
        }
    }

    /**
     * Validates whether the incident type allows
     * pending allocation.
     *
     * @param incident the incident
     */
    public void validatePendingAllocation(Incident incident) {

        String incidentType =
                incident.getIncidenttype().getName();

        if (!"Mechanical Breakdown".equalsIgnoreCase(incidentType)
                && !"Accident".equalsIgnoreCase(incidentType)) {

            throw new BusinessRuleViolationException(
                    "Only MECHANICAL BREAKDOWN or ACCIDENT incidents can be "
                            + "marked for PENDING ALLOCATION"
            );
        }
    }

    private String normalize(String status) {
        return status.trim().toUpperCase();
    }

    public void validateInitialStatus(
            IncidentStatus status) {

        String statusName =
                normalize(status.getName());

        if (!"REPORTED".equals(statusName)) {
            throw new BusinessRuleViolationException(
                    "New incidents must start with REPORTED status"
            );
        }
    }
}