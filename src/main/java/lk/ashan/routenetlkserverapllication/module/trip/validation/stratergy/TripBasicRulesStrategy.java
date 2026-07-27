package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
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

/**
 * Strategy implementation for validating basic rules related to trip creation.
 * This class ensures that all business rules are adhered to when creating or updating trips.
 */
@Component
@RequiredArgsConstructor
public class TripBasicRulesStrategy implements TripValidationStrategy {

    private final TripRepository tripRepository;
    private final PermitRepository permitRepository;
    private final RouteRepository routeRepository;
    private final OriginTerminalRepository originTerminalRepository;

    /**
     * Validates the creation of a trip based on various business rules.
     *
     * @param context the context containing trip details and validation parameters
     * @throws BusinessRuleViolationException if any business rule is violated
     */
    @Override
    public void validateCreate(TripValidationContext context) {
        validateTimeLogic(context);
        validateSameRouteTripsMinGaps(context);
        validateIdempotency(context);
        validatePermittedDailyTripQuota(context);
        validateTripOverlap(context);
        validateTerminalLocation(context);
        validatePermittedDailyTripQuota(context);

    }

    /**
     * Validates the time logic of the trip, ensuring proper departure and arrival times.
     *
     * @param context the context containing trip details
     * @throws BusinessRuleViolationException if the trip duration is zero or if the trip type and times are inconsistent
     */
    private void validateTimeLogic(TripValidationContext context) {
        LocalTime dep = context.getDeparture();
        LocalTime arr = context.getArrival();

        final Integer MIDNIGHT_TRIP_TYPE = 5;
        boolean isOverNightType = MIDNIGHT_TRIP_TYPE.equals(context.getTriptypeId());

        if (dep.equals(arr)) {
            throw new BusinessRuleViolationException("Trip duration cannot be zero.");
        }

        boolean numericalMidnightDetected = arr.isBefore(dep);

        if (numericalMidnightDetected && !isOverNightType) {
            throw new BusinessRuleViolationException("Arrival time is before departure, but this is not marked as a Midnight Trip.");
        }

        if (!numericalMidnightDetected && isOverNightType) {
            throw new BusinessRuleViolationException("This is marked as a Midnight Trip, but the arrival time is not after midnight.");
        }
    }


/**
     * Validates that there is sufficient time gap between the departure of the new trip
     * and the departure of existing trips on the same route.
     *
     * @param context the context containing trip details
     * @throws ResourceNotFoundException if the route is not found
     * @throws BusinessRuleViolationException if the time gap between trips is less than the minimum allowed
     */
    private void validateSameRouteTripsMinGaps(TripValidationContext context) {

        Route permitRoute = routeRepository.findById(context.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"));

        List<Trip> sameRouteTrips = tripRepository.findByPermite_Route_Id(context.getId());

        for (Trip existing : sameRouteTrips) {
            if (context.getId() != null && existing.getId().equals(context.getId())) {
                continue;
            }

            long gap = calculateCircularGap(existing.getTodepature(), context.getDeparture());

            if (gap < permitRoute.getMingapminutes()) {
                throw new BusinessRuleViolationException(
                        String.format("Gap violation! Only %d minutes from trip at %s", gap, existing.getTodepature())
                );
            }
        }
    }

    /**
     * Calculates the circular gap between two times, considering midnight crossings.
     *
     * @param t1 the first time
     * @param t2 the second time
     * @return the gap in minutes
     */
    private long calculateCircularGap(LocalTime t1, LocalTime t2) {
        long diff = Math.abs(Duration.between(t1, t2).toMinutes());
        return (diff > 720) ? (1440 - diff) : diff;
    }

    /**
     * Validates idempotency by checking for duplicate trips with the same core attributes.
     *
     * @param context the context containing trip details
     * @throws BusinessRuleViolationException if a duplicate trip is detected
     */
    private void validateIdempotency(TripValidationContext context) {
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
     * Validates that the number of active trips does not exceed the daily quota allowed by the permit.
     *
     * @param context the context containing trip details
     * @throws BusinessRuleViolationException if the daily trip quota is exceeded
     */
    private void validatePermittedDailyTripQuota(TripValidationContext context) {
        int allowedQuota = permitRepository.findById(context.getPermitId())
                .orElseThrow(() -> new ResourceNotFoundException("Permit Not Found"))
                .getNotripsperday();

        long activeCount = tripRepository.countByPermite_IdAndTripstatus_Name(
                context.getPermitId(),
                "Active"
        );

        if (activeCount >= allowedQuota) {
            throw new BusinessRuleViolationException(
                    String.format("Permit Quota Exceeded! This permit only allows %d trips per day. " +
                                    "Currently, there are %d active trip templates.",
                            allowedQuota, activeCount)
            );
        }
    }

    /**
     * Validates that the new trip does not overlap with any existing trips for the same permit.
     * Ensures that no two trips for the same permit have overlapping time ranges, considering
     * overnight trips and midnight crossings.
     *
     * @param context the context containing trip details
     * @throws BusinessRuleViolationException if a scheduling conflict is detected
     */
    private void validateTripOverlap(TripValidationContext context) {

        List<Trip> existingTrips = tripRepository.findByPermite_Id(
                context.getPermitId()
        );

        LocalTime newDep = context.getDeparture();
        LocalTime newArr = context.getArrival();

        boolean isNewOvernight = Integer.valueOf(5).equals(context.getTriptypeId());

        for (Trip existing : existingTrips) {

            // Skip current trip during update
            if (context.getId() != null && existing.getId().equals(context.getId())) {
                continue;
            }

            LocalTime exDep = existing.getTodepature();
            LocalTime exArr = existing.getToarrival();

            boolean isExOvernight =
                    Integer.valueOf(5).equals(existing.getTriptype().getId());

            if (isOverlapping(
                    newDep,
                    newArr,
                    isNewOvernight,
                    exDep,
                    exArr,
                    isExOvernight
            )) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Scheduling Conflict! This permit already has a trip from %s to %s. " +
                                        "A bus cannot operate two trips simultaneously.",
                                exDep,
                                exArr
                        )
                );
            }
        }
    }

    /**
     * Checks if two time ranges overlap, considering midnight crossings.
     *
     * @param start1 the start time of the first range
     * @param end1 the end time of the first range
     * @param over1 whether the first range crosses midnight
     * @param start2 the start time of the second range
     * @param end2 the end time of the second range
     * @param over2 whether the second range crosses midnight
     * @return true if the time ranges overlap, false otherwise
     */
    private boolean isOverlapping(LocalTime start1, LocalTime end1, boolean over1,
                                  LocalTime start2, LocalTime end2, boolean over2) {
        long s1 = start1.toSecondOfDay() / 60;
        long e1 = (over1) ? (end1.toSecondOfDay() / 60) + 1440 : end1.toSecondOfDay() / 60;

        long s2 = start2.toSecondOfDay() / 60;
        long e2 = (over2) ? (end2.toSecondOfDay() / 60) + 1440 : end2.toSecondOfDay() / 60;

        return (s1 < e2) && (e1 > s2);
    }

    /**
     * Validates that the terminal location is within the route authorized by the permit.
     *
     * @param context the context containing trip details
     * @throws BusinessRuleViolationException if the terminal location is not authorized for the route
     * @throws ResourceNotFoundException if the permit or terminal is not found
     */
    private void validateTerminalLocation(TripValidationContext context) {
        Permite permite = permitRepository.findById(context.getPermitId())
                .orElseThrow(() -> new ResourceNotFoundException("Permit Not Found"));
        String routeOrigin = permite.getRoute().getOrigin();
        String routeDestination = permite.getRoute().getDestination();

        Originterminal tripOriginTerminal = originTerminalRepository.findById(context.getOriginTerminalId())
                .orElseThrow(() -> new ResourceNotFoundException("Origin not found"));

        String terminalCity = tripOriginTerminal.getCity();

        boolean isValidLocation = terminalCity.equalsIgnoreCase(routeOrigin) ||
                terminalCity.equalsIgnoreCase(routeDestination);

        if (!isValidLocation) {
            throw new BusinessRuleViolationException(
                    String.format("Terminal Mismatch! The terminal is in %s, " +
                                    "but this permit is only authorized for the %s - %s route.", terminalCity, routeOrigin, routeDestination)
            );
        }
    }

/**
     * Validates the sequence of trips for a permit, ensuring that there is sufficient turnaround time
     * between consecutive trips and that no conflicts arise.
     *
     * @param context the context containing trip details
     * @throws ResourceNotFoundException if the route is not found
     * @throws BusinessRuleViolationException if the turnaround time between trips is insufficient
     */
    private void validatePermitTripSequence(TripValidationContext context) {
        List<Trip> existingTrips = tripRepository.findByPermite_Id(context.getPermitId());

        LocalTime newDeparture = context.getDeparture();
        LocalTime newArrival = context.getArrival();

        boolean newOvernight = Integer.valueOf(5).equals(context.getTriptypeId());

        int minGapMinutes = routeRepository.findById(context.getRouteId())
                .orElseThrow(() -> new ResourceNotFoundException("Route not found"))
                .getMingapminutes();

        for (Trip existing : existingTrips) {
            // update case
            if (context.getId() != null && existing.getId().equals(context.getId()))
                continue;

            LocalTime existingDeparture = existing.getTodepature();
            LocalTime existingArrival = existing.getToarrival();

            boolean existingOvernight = Integer.valueOf(5).equals(existing.getTriptype().getId());

            /*
             * Existing trip finishes -> New trip starts
             */
            long forwardGap = calculateTimeGap(existingArrival, newDeparture, existingOvernight);

            /*
             * New trip finishes -> Existing trip starts
             */
            long backwardGap = calculateTimeGap(newArrival, existingDeparture, newOvernight);

            if (forwardGap >= 0 && forwardGap < minGapMinutes) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Insufficient turnaround time. Previous trip ends at %s. " +
                                "Minimum required gap is %d minutes.",
                                existingArrival,
                                minGapMinutes
                        )
                );
            }

            if (backwardGap >= 0 && backwardGap < minGapMinutes) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Insufficient turnaround time before existing trip starts at %s.",
                                existingDeparture
                        )
                );
            }
        }
    }

    /**
     * Calculates the time gap between two times, considering whether the time range crosses midnight.
     *
     * @param end the end time of the first range
     * @param start the start time of the second range
     * @param overnight whether the time range crosses midnight
     * @return the time gap in minutes
     */
    private long calculateTimeGap(LocalTime end, LocalTime start, boolean overnight) {
        long endMinutes = end.toSecondOfDay() / 60;
        long startMinutes = start.toSecondOfDay() / 60;

        if (overnight && startMinutes < endMinutes) startMinutes += 1440;

        return startMinutes - endMinutes;
    }
}
