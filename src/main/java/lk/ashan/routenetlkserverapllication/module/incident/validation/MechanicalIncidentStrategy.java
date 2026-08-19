package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Strategy implementation for handling mechanical incidents.
 * Validates the incident context and determines applicability based on the type code.
 */
@Component
@RequiredArgsConstructor
public class MechanicalIncidentStrategy implements IncidentStrategy {

    private final TripExecutionRepository tripExecutionRepository;

    /**
     * Validates the incident context for mechanical incidents.
     *
     * @param context the incident context containing details of the incident
     * @throws BusinessRuleViolationException if the incident odometer is less than the trip's start odometer
     */
    @Override
    public void validate(IncidentContext context) {
        TripExecution trip = tripExecutionRepository.findById(context.getTripId()).orElseThrow();

        if (context.getOdometerAtIncident() < trip.getStartodometer()) {
            throw new BusinessRuleViolationException("Incident odometer cannot be less than start odometer");
        }
    }

    /**
     * Checks if this strategy is applicable for the given type code.
     *
     * @param typeCode the type code of the incident
     * @return true if the type code matches "MECHANICAL", false otherwise
     */
    @Override
    public boolean isApplicable(String typeCode) {
        return "MECHANICAL".equalsIgnoreCase(typeCode);
    }
}
