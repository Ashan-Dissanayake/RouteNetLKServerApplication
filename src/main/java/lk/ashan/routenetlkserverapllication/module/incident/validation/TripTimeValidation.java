package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * Validates the time of an incident against the trip execution's schedule.
 * Ensures that the reported time of the incident falls within the trip's duration.
 */
@Component
@RequiredArgsConstructor
public class TripTimeValidation implements IncidentStrategy {

    private final TripExecutionRepository tripExecutionRepository;

    /**
     * Validates the incident context to ensure the reported time is within the trip's duration.
     *
     * @param context the incident context containing the trip ID and reported time
     * @throws BusinessRuleViolationException if the trip execution is not found or the reported time is outside the trip's duration
     */
    @Override
    public void validate(IncidentContext context) {
        TripExecution tripExecution = tripExecutionRepository.findById(context.getTripId())
                .orElseThrow(() -> new BusinessRuleViolationException("Trip Execution not found"));
        LocalTime reported = context.getReportedTime();
        if (reported.isBefore(tripExecution.getTrip().getTodepature()) ||
                reported.isAfter(tripExecution.getTrip().getToarrival())) {
            throw new BusinessRuleViolationException("Incident time must be within trip duration");
        }
    }

    /**
     * Determines if this validation strategy is applicable for the given type code.
     *
     * @param typeCode the type code of the incident
     * @return true if this validation strategy is applicable, false otherwise
     */
    @Override
    public boolean isApplicable(String typeCode) {
        return true;
    }
}
