package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MechanicalIncidentStrategy implements IncidentStrategy {

    private final TripExecutionRepository tripExecutionRepository;

    @Override
    public void validate(IncidentContext context) {
        TripExecution trip = tripExecutionRepository.findById(context.getTripId()).orElseThrow();

        if (context.getOdometerAtIncident() < trip.getStartodometer()) {
            throw new BusinessRuleViolationException("Incident odometer cannot be less than start odometer");
        }
    }

    @Override
    public boolean isApplicable(String typeCode) {
        return "MECHANICAL".equalsIgnoreCase(typeCode);
    }
}
