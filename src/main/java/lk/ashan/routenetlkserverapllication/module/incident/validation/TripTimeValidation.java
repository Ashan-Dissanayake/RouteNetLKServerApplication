package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TripTimeValidation implements IncidentCreationStrategy {

    private final TripRepository tripRepository;

    @Override
    public void validate(IncidentContext context) {
        Trip trip = tripRepository.findById(context.getTripId())
                .orElseThrow(() -> new BusinessRuleViolationException("Trip not found"));
        LocalTime reported = context.getReportedTime();
        if (reported.isBefore(trip.getTodepature()) ||
                reported.isAfter(trip.getToarrival())) {
            throw new BusinessRuleViolationException("Incident time must be within trip duration");
        }
    }
}
