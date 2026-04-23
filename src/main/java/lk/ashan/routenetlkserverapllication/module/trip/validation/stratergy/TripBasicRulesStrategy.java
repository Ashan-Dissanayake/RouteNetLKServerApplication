package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class TripBasicRulesStrategy implements TripValidationStrategy {

    private final TripRepository tripRepository;

    @Override
    public void validateCreate(TripValidationContext context) {
        validateTimeLogic(context);
        validateTerminalGaps(context);
        validateIdempotency(context);
    }

    private void validateTimeLogic(TripValidationContext context) {
        LocalTime dep = context.getDeparture();
        LocalTime arr = context.getArrival();

        // Using a constant or Enum for TripType is safer than a magic number like '5'
        final Integer MIDNIGHT_TRIP_TYPE = 5;
        boolean isOverNightType = MIDNIGHT_TRIP_TYPE.equals(context.getTriptypeId());

        // 1. Prevent zero-duration trips
        if (dep.equals(arr)) {
            throw new BusinessRuleViolationException("Trip duration cannot be zero.");
        }

        // 2. Numerical Check: Arrival is before Departure (e.g., 22:00 to 02:00)
        boolean numericalMidnightDetected = arr.isBefore(dep);

        if (numericalMidnightDetected && !isOverNightType) {
            // The times cross midnight, but the trip isn't categorized as a Midnight Trip
            throw new BusinessRuleViolationException("Arrival time is before departure, but this is not marked as a Midnight Trip.");
        }

        if (!numericalMidnightDetected && isOverNightType) {
            // It's marked as a Midnight Trip, but the times are on the same day (e.g., 08:00 to 10:00)
            throw new BusinessRuleViolationException("This is marked as a Midnight Trip, but the arrival time is not after midnight.");
        }
    }
    private void validateTerminalGaps(TripValidationContext context) {
        for (Trip existing : context.getExistingTripsAtTerminal()) {
            // Skip self on update
            if (context.getId() != null && existing.getId().equals(context.getId())) {
                continue;
            }

            // Midnight-aware gap calculation
            long gap = calculateCircularGap(existing.getTodepature(), context.getDeparture());

            if (gap < context.getMinGapMinutes()) {
                throw new BusinessRuleViolationException(
                        String.format("Gap violation! Only %d minutes from trip at %s", gap, existing.getTodepature())
                );
            }
        }
    }

    private long calculateCircularGap(LocalTime t1, LocalTime t2) {
        long diff = Math.abs(Duration.between(t1, t2).toMinutes());
        // If the difference is > 12 hours (720 mins), the "shorter" gap
        // is actually across the midnight boundary.
        return (diff > 720) ? (1440 - diff) : diff;
    }

    private void validateIdempotency(TripValidationContext context) {
        // Search for any existing trip with the same core attributes
        boolean exists = tripRepository.existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrival(
                context.getPermitId(),
                context.getOriginTerminalId(),
                context.getDeparture(),
                context.getArrival()
        );

        if (exists) {
            throw new BusinessRuleViolationException(
                    String.format("Duplicate Trip Detected! A schedule already exists for Permit %s starting at %s.",
                            context.getPermitId(), context.getDeparture())
            );
        }
    }
}
