package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OriginTerminalRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TripBasicRulesStrategy implements TripValidationStrategy {

    private final TripRepository tripRepository;
    private final PermitRepository permitRepository;
    private final OriginTerminalRepository originTerminalRepository;

    @Override
    public void validateCreate(TripValidationContext context) {
        validateTimeLogic(context);
        validateTerminalGaps(context);
        validateIdempotency(context);
        validatePermittedDailyTripQuota(context);
        validateTripOverlap(context);
        validateTerminalLocation(context);
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
        boolean exists = tripRepository.existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                context.getPermitId(),
                context.getOriginTerminalId(),
                context.getDeparture(),
                context.getArrival(),
                "Active"
        );

        if (exists) {
            throw new BusinessRuleViolationException(
                    String.format("Duplicate Trip Detected! A schedule already exists for Permit %s starting at %s.",
                            context.getPermitId(), context.getDeparture())
            );
        }
    }


    /**
     * Ensures the number of active trip templates does not exceed
     * the total trips per day authorized by the NTC permit.
     */
    private void validatePermittedDailyTripQuota(TripValidationContext context) {
        // 1. Get the maximum allowed trips from the Permit module
//        int allowedQuota = context.getPermit().getTripsPerDay();
        int allowedQuota = permitRepository.findById(context.getPermitId())
                .orElseThrow(()->new ResourceNotFoundException("Permit Not Found"))
                .getNotripsperday();

        // 2. Count current active trips for this permit
        // Exclude the current trip ID if this is an update/replacement scenario
        long activeCount = tripRepository.countByPermite_IdAndTripstatus_Name(
                context.getPermitId(),
                "Active"
        );

        // 3. Logic check
        // If we are creating a new trip, we check if adding one more exceeds the quota.
        // Note: In a 'Replacement' flow, the old trip is discontinued first,
        // so the count should stay within limits.
        if (activeCount >= allowedQuota) {
            throw new BusinessRuleViolationException(
                    String.format("Permit Quota Exceeded! This permit only allows %d trips per day. " +
                                    "Currently, there are %d active trip templates.",
                            allowedQuota, activeCount)
            );
        }
    }

    /**
     * Ensures that for a single permit, trips do not overlap in time.
     * This handles the reality that one bus cannot be in two places at once.
     */
    private void validateTripOverlap(TripValidationContext context) {
        // Fetch all currently active templates for this permit
        List<Trip> activeTrips = tripRepository.findByPermite_IdAndTripstatus_Name(
                context.getPermitId(),
                "Active"
        );

        LocalTime newDep = context.getDeparture();
        LocalTime newArr = context.getArrival();
        boolean isNewOvernight = Integer.valueOf(5).equals(context.getTriptypeId());

        for (Trip existing : activeTrips) {
            LocalTime exDep = existing.getTodepature();
            LocalTime exArr = existing.getToarrival();
            boolean isExOvernight = Integer.valueOf(5).equals(existing.getTriptype().getId());

            // We use a helper to see if the two time windows intersect
            if (isOverlapping(newDep, newArr, isNewOvernight, exDep, exArr, isExOvernight)) {
                throw new BusinessRuleViolationException(
                        String.format("Scheduling Conflict! This permit is already active for a trip from %s to %s. " +
                                "A bus cannot operate two trips simultaneously.", exDep, exArr)
                );
            }
        }
    }

    /**
     * Helper to check if two time ranges overlap, considering midnight crossings.
     */
    private boolean isOverlapping(LocalTime start1, LocalTime end1, boolean over1,
                                  LocalTime start2, LocalTime end2, boolean over2) {

        // Convert to a comparable "minutes from midnight" scale
        // If overnight, we treat the arrival as being on 'Day 2' (adding 1440 minutes)
        long s1 = start1.toSecondOfDay() / 60;
        long e1 = (over1) ? (end1.toSecondOfDay() / 60) + 1440 : end1.toSecondOfDay() / 60;

        long s2 = start2.toSecondOfDay() / 60;
        long e2 = (over2) ? (end2.toSecondOfDay() / 60) + 1440 : end2.toSecondOfDay() / 60;

        // Standard overlap formula: (StartA < EndB) AND (EndA > StartB)
        return (s1 < e2) && (e1 > s2);
    }

    private void validateTerminalLocation(TripValidationContext context) {
        // 1. Get the Route cities from the Permit
       Permite permite = permitRepository.findById(context.getPermitId())
                .orElseThrow(()->new ResourceNotFoundException("Permit Not Found"));
        String routeOrigin = permite.getRoute().getOrigin();
        String routeDestination = permite.getRoute().getDestination();

        Originterminal tripOriginTerminal = originTerminalRepository.findById(context.getOriginTerminalId())
                .orElseThrow(()-> new ResourceNotFoundException("Origin not found"));

        // 2. Get the City where the selected Terminal is located
        String terminalCity = tripOriginTerminal.getCity();

        // 3. Validation: Is the terminal in one of the allowed cities for this route?
        boolean isValidLocation = terminalCity.equalsIgnoreCase(routeOrigin) ||
                terminalCity.equalsIgnoreCase(routeDestination);

        if (!isValidLocation) {
            throw new BusinessRuleViolationException(
                    String.format("Terminal Mismatch! The terminal is in %s, " +
                                    "but this permit is only authorized for the %s - %s route.", terminalCity, routeOrigin, routeDestination)
            );
        }
    }

    /* To Implemente in future
    1. The "Day-Crossing" Turnaround (The 48-Hour Loop)
       Case: A bus departs Colombo at 11:00 PM (Monday) and arrives in Jaffna at 5:00 AM (Tuesday)
       The Edge Case: The system must recognize that even though it is now Tuesday, the bus is "occupied"
       by a Monday-started trip.

        Business Rule: You cannot schedule a return trip from Jaffna at 4:00 AM Tuesday because the bus
        hasn't arrived yet,even though 4:00 AM is technically "later" in the day than the 11:00 PM start.

        Your Solution: Your minutes-from-midnight logic (adding 1440 minutes for Type 5 trips) handles
        this temporal continuity.
     */
}
