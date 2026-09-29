package lk.ashan.routenetlkserverapllication.module.trip.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Originterminal;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.repository.OriginTerminalRepository;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TripValidator {

    private static final Integer MIDNIGHT_TRIP_TYPE_ID = 5;

    private final TripRepository tripRepository;
    private final PermitRepository permitRepository;
    private final RouteRepository routeRepository;
    private final OriginTerminalRepository originTerminalRepository;
    private final TripExecutionService tripExecutionService;

    public void validateCreate(Trip trip) {
        validateTimeLogic(trip);
        validateSameRouteTripsMinGaps(trip);
        validateIdempotency(trip);
        validatePermittedDailyTripQuota(trip);
        validateTripOverlap(trip);
        validateTerminalLocation(trip);
        validatePermitTripSequence(trip);
    }

    public void validateActivation(Trip trip) {

        String currentStatus =
                trip.getPermite()
                        .getVehicle()
                        .getVehiclestatus()
                        .getName()
                        .trim()
                        .toUpperCase();

        List<String> allowedStatuses = List.of("AVAILABLE", "ALLOCATED");

        if (!allowedStatuses.contains(currentStatus)) {
            throw new BusinessRuleViolationException(
                    String.format(
                            "Cannot activate trip. Vehicle is currently in '%s' status.",
                            currentStatus
                    )
            );
        }

        boolean exists =
                tripRepository.existsActiveTrip(
                        trip.getPermite().getRoute().getId(),
                        trip.getOriginterminal().getId(),
                        trip.getTodepature(),
                        trip.getId()
                );

        if (exists) {
            throw new BusinessRuleViolationException(
                    "An active trip already exists for the same route, "
                            + "origin terminal and departure time."
            );
        }
    }

    public void validateSuspension(Trip trip) {

        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        boolean hasActiveTrips =
                tripExecutions.stream()
                        .anyMatch(execution ->
                                "IN PROGRESS".equalsIgnoreCase(
                                        execution
                                                .getTripexecutionstatus()
                                                .getName()
                                )
                        );

        if (hasActiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot suspend the Master Schedule because "
                            + "there are trips currently 'IN PROGRESS' on the road."
            );
        }
    }

    public void validateDiscontinuation(Trip trip) {

        List<TripExecution> tripExecutions = tripExecutionService.getTripExecutionByTripId(trip.getId());

        boolean hasLiveTrips =
                tripExecutions.stream()
                        .anyMatch(execution ->
                                "IN PROGRESS".equalsIgnoreCase(
                                        execution
                                                .getTripexecutionstatus()
                                                .getName()
                                )
                        );

        if (hasLiveTrips) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: A vehicle is currently "
                            + "performing a journey for this route."
            );
        }

        boolean hasUnsettledAccounts =
                tripExecutions.stream()
                        .anyMatch(execution ->
                                "COMPLETED".equalsIgnoreCase(
                                        execution
                                                .getTripexecutionstatus()
                                                .getName()
                                )
                        );

        if (hasUnsettledAccounts) {
            throw new BusinessRuleViolationException(
                    "Cannot discontinue: There are completed trips "
                            + "awaiting fare collection/settlement."
            );
        }
    }

    public void validateStatusTransition(Tripstatus currentStatus, Tripstatus targetStatus) {

        String current = normalize(currentStatus.getName());

        String target = normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "DRAFT" ->
                    target.equals("ACTIVE")
                            || target.equals("CANCELLED");

            case "ACTIVE" ->
                    target.equals("SUSPENDED")
                            || target.equals("DISCONTINUED")
                            || target.equals("DRAFT");

            case "SUSPENDED" ->
                    target.equals("ACTIVE")
                            || target.equals("DISCONTINUED");

            case "DISCONTINUED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new InvalidStateTransitionException(
                    "Invalid status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    private void validateTimeLogic(Trip trip) {

        LocalTime departure = trip.getTodepature();
        LocalTime arrival = trip.getToarrival();

        boolean isOvernight =
                MIDNIGHT_TRIP_TYPE_ID.equals(
                        trip.getTriptype().getId()
                );

        if (departure.equals(arrival)) {
            throw new BusinessRuleViolationException(
                    "Trip duration cannot be zero."
            );
        }

        boolean arrivalBeforeDeparture = arrival.isBefore(departure);

        if (arrivalBeforeDeparture && !isOvernight) {
            throw new BusinessRuleViolationException(
                    "Arrival time is before departure, "
                            + "but this is not marked as a Midnight Trip."
            );
        }

        if (!arrivalBeforeDeparture && isOvernight) {
            throw new BusinessRuleViolationException(
                    "This is marked as a Midnight Trip, "
                            + "but the arrival time is not after midnight."
            );
        }
    }

    private void validateSameRouteTripsMinGaps(Trip trip) {

        Route route = routeRepository.findById(
                                trip.getPermite().getRoute().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Route not found"
                                )
                        );

        List<Trip> sameRouteTrips =
                tripRepository.findByPermite_Route_Id(
                        trip.getPermite().getRoute().getId()
                );

        for (Trip existing : sameRouteTrips) {

            if (trip.getId() != null
                    && existing.getId().equals(trip.getId())) {
                continue;
            }

            long gap =
                    calculateCircularGap(
                            existing.getTodepature(),
                            trip.getTodepature()
                    );

            if (gap < route.getMingapminutes()) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Gap violation! Only %d minutes from trip at %s",
                                gap,
                                existing.getTodepature()
                        )
                );
            }
        }
    }

    private long calculateCircularGap(LocalTime first, LocalTime second) {
        long difference =
                Math.abs(
                        Duration
                                .between(first, second)
                                .toMinutes()
                );

        return difference > 720
                ? 1440 - difference
                : difference;
    }

    private void validateIdempotency(Trip trip) {

        boolean exists =
                tripRepository
                        .existsByPermite_IdAndOriginterminal_IdAndTodepatureAndToarrivalAndTripstatus_Name(
                                trip.getPermite().getId(),
                                trip.getOriginterminal().getId(),
                                trip.getTodepature(),
                                trip.getToarrival(),
                                "Active"
                        );

        if (exists) {
            throw new BusinessRuleViolationException(
                    String.format(
                            "Duplicate Trip Detected! A schedule already exists "
                                    + "for Permit %s starting at %s.",
                            trip.getPermite().getId(),
                            trip.getTodepature()
                    )
            );
        }
    }

    private void validatePermittedDailyTripQuota(Trip trip) {

        int allowedQuota = permitRepository
                        .findById(trip.getPermite().getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permit Not Found"
                                )
                        )
                        .getNotripsperday();

        long activeCount = tripRepository
                        .countByPermite_IdAndTripstatus_Name(
                                trip.getPermite().getId(),
                                "Active"
                        );

        if (activeCount >= allowedQuota) {
            throw new BusinessRuleViolationException(
                    String.format(
                            "Permit Quota Exceeded! This permit only allows "
                                    + "%d trips per day. Currently, there are "
                                    + "%d active trip templates.",
                            allowedQuota,
                            activeCount
                    )
            );
        }
    }

    private void validateTripOverlap(Trip trip) {

        List<Trip> existingTrips = tripRepository.findByPermite_Id(
                        trip.getPermite().getId()
                );

        LocalTime newDeparture = trip.getTodepature();

        LocalTime newArrival = trip.getToarrival();

        boolean newOvernight = MIDNIGHT_TRIP_TYPE_ID.equals(
                        trip.getTriptype().getId()
                );

        for (Trip existing : existingTrips) {

            if (trip.getId() != null
                    && existing.getId().equals(trip.getId())) {
                continue;
            }

            boolean existingOvernight = MIDNIGHT_TRIP_TYPE_ID.equals(
                            existing.getTriptype().getId()
                    );

            if (isOverlapping(
                    newDeparture,
                    newArrival,
                    newOvernight,
                    existing.getTodepature(),
                    existing.getToarrival(),
                    existingOvernight
            )) {
                throw new BusinessRuleViolationException(
                        String.format(
                                "Scheduling Conflict! This permit already has "
                                        + "a trip from %s to %s. A bus cannot "
                                        + "operate two trips simultaneously.",
                                existing.getTodepature(),
                                existing.getToarrival()
                        )
                );
            }
        }
    }

    private boolean isOverlapping(
            LocalTime start1,
            LocalTime end1,
            boolean overnight1,
            LocalTime start2,
            LocalTime end2,
            boolean overnight2
    ) {
        long startMinute1 = start1.toSecondOfDay() / 60;

        long endMinute1 = overnight1
                        ? end1.toSecondOfDay() / 60 + 1440
                        : end1.toSecondOfDay() / 60;

        long startMinute2 = start2.toSecondOfDay() / 60;

        long endMinute2 = overnight2
                        ? end2.toSecondOfDay() / 60 + 1440
                        : end2.toSecondOfDay() / 60;

        return startMinute1 < endMinute2 && endMinute1 > startMinute2;
    }

    private void validateTerminalLocation(Trip trip) {

        Permite permit = permitRepository.findById(trip.getPermite().getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Permit Not Found"
                                )
                        );

        String routeOrigin = permit.getRoute().getOrigin();

        String routeDestination = permit.getRoute().getDestination();

        Originterminal terminal = originTerminalRepository.findById(
                                trip.getOriginterminal().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Origin not found"
                                )
                        );

        String terminalCity = terminal.getCity();

        boolean valid = terminalCity.equalsIgnoreCase(routeOrigin)
                        || terminalCity.equalsIgnoreCase(routeDestination);

        if (!valid) {
            throw new BusinessRuleViolationException(
                    String.format(
                            "Terminal Mismatch! The terminal is in %s, "
                                    + "but this permit is only authorized for "
                                    + "the %s - %s route.",
                            terminalCity,
                            routeOrigin,
                            routeDestination
                    )
            );
        }
    }

    private void validatePermitTripSequence(Trip trip) {

        List<Trip> existingTrips = tripRepository.findByPermite_Id(
                        trip.getPermite().getId()
                );

        LocalTime newDeparture = trip.getTodepature();

        LocalTime newArrival = trip.getToarrival();

        boolean newOvernight =
                MIDNIGHT_TRIP_TYPE_ID.equals(
                        trip.getTriptype().getId()
                );

        int minGapMinutes =
                routeRepository.findById(
                                trip.getPermite().getRoute().getId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Route not found"
                                )
                        )
                        .getMingapminutes();

        for (Trip existing : existingTrips) {

            if (trip.getId() != null && existing.getId().equals(trip.getId())) {
                continue;
            }

            LocalTime existingDeparture = existing.getTodepature();

            LocalTime existingArrival = existing.getToarrival();

            boolean existingOvernight =
                    MIDNIGHT_TRIP_TYPE_ID.equals(
                            existing.getTriptype().getId()
                    );

            long forwardGap =
                    calculateTimeGap(
                            existingArrival,
                            newDeparture,
                            existingOvernight
                    );

            long backwardGap =
                    calculateTimeGap(
                            newArrival,
                            existingDeparture,
                            newOvernight
                    );

            if (forwardGap >= 0 && forwardGap < minGapMinutes) {

                throw new BusinessRuleViolationException(
                        String.format(
                                "Insufficient turnaround time. Previous trip "
                                        + "ends at %s. Minimum required gap is "
                                        + "%d minutes.",
                                existingArrival,
                                minGapMinutes
                        )
                );
            }

            if (backwardGap >= 0 && backwardGap < minGapMinutes) {

                throw new BusinessRuleViolationException(
                        String.format(
                                "Insufficient turnaround time before existing "
                                        + "trip starts at %s.",
                                existingDeparture
                        )
                );
            }
        }
    }

    private long calculateTimeGap(
            LocalTime end,
            LocalTime start,
            boolean overnight
    ) {
        long endMinutes = end.toSecondOfDay() / 60;

        long startMinutes = start.toSecondOfDay() / 60;

        if (overnight && startMinutes < endMinutes) {
            startMinutes += 1440;
        }

        return startMinutes - endMinutes;
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }
}
