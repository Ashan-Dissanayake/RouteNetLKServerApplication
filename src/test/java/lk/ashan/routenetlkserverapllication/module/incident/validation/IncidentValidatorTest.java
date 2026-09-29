package lk.ashan.routenetlkserverapllication.module.incident.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentType;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class IncidentValidatorTest {

    @InjectMocks
    private IncidentValidator validator;


    // =========================
    // CREATE - INCIDENT TIME
    // =========================

    @Test
    void validateCreate_shouldThrowExceptionWhenReportedTimeIsBeforeTripDeparture() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(12, 0))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(9, 59))
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request, tripExecution)
        );
    }

    @Test
    void validateCreate_shouldThrowExceptionWhenReportedTimeIsAfterTripArrival() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(12, 0))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(12, 1))
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateCreate(request, tripExecution)
        );
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenReportedTimeIsWithinTripDuration() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(12, 0))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(11, 0))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateCreate(request, tripExecution)
        );
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenReportedTimeEqualsDepartureTime() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(12, 0))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(10, 0))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateCreate(request, tripExecution)
        );
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenReportedTimeEqualsArrivalTime() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(12, 0))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(12, 0))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateCreate(request, tripExecution)
        );
    }

    @Test
    void validateCreate_shouldNotThrowExceptionWhenTripDurationIsOneMinute() {

        TripExecution tripExecution =
                TripExecution.builder()
                        .trip(
                                Trip.builder()
                                        .todepature(LocalTime.of(10, 0))
                                        .toarrival(LocalTime.of(10, 1))
                                        .build()
                        )
                        .build();

        IncidentCreateRequestDto request =
                IncidentCreateRequestDto.builder()
                        .toreported(LocalTime.of(10, 0))
                        .build();

        assertDoesNotThrow(
                () -> validator.validateCreate(request, tripExecution)
        );
    }


    // =========================
    // STATUS TRANSITIONS
    // =========================

    @Test
    void validateStatusTransition_shouldAllowSameStatusTransition() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("REPORTED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(status, status)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowReportedToInProgress() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("REPORTED")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowInProgressToVehicleRecovery() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("VEHICLE RECOVERY")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowInProgressToPendingAllocation() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("PENDING ALLOCATION")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowInProgressToResolved() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowVehicleRecoveryToPendingAllocation() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("VEHICLE RECOVERY")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("PENDING ALLOCATION")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowVehicleRecoveryToResolved() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("VEHICLE RECOVERY")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowPendingAllocationToResolved() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("PENDING ALLOCATION")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldAllowResolvedToClosed() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenReportedTransitionsDirectlyToResolved() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("REPORTED")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenReportedTransitionsDirectlyToClosed() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("REPORTED")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenInProgressTransitionsDirectlyToClosed() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenPendingAllocationTransitionsToClosed() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("PENDING ALLOCATION")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenClosedTransitionsToAnyStatus() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldThrowExceptionWhenUnknownCurrentStatusIsUsed() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("UNKNOWN")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(current, target)
        );
    }


    // =========================
    // STATUS NORMALIZATION
    // =========================

    @Test
    void validateStatusTransition_shouldIgnoreCaseWhenComparingStatuses() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("reported")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("in progress")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }

    @Test
    void validateStatusTransition_shouldIgnoreLeadingAndTrailingSpaces() {

        IncidentStatus current =
                IncidentStatus.builder()
                        .name("  REPORTED  ")
                        .build();

        IncidentStatus target =
                IncidentStatus.builder()
                        .name("  IN PROGRESS  ")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateStatusTransition(current, target)
        );
    }


    // =========================
    // VEHICLE RECOVERY
    // =========================

    @Test
    void validateVehicleRecovery_shouldAllowMechanicalBreakdown() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Mechanical Breakdown")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validateVehicleRecovery(incident)
        );
    }

    @Test
    void validateVehicleRecovery_shouldAllowAccident() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Accident")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validateVehicleRecovery(incident)
        );
    }

    @Test
    void validateVehicleRecovery_shouldThrowExceptionForOtherIncidentType() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Traffic Violation")
                                        .build()
                        )
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateVehicleRecovery(incident)
        );
    }

    @Test
    void validateVehicleRecovery_shouldIgnoreCaseForMechanicalBreakdown() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("mechanical breakdown")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validateVehicleRecovery(incident)
        );
    }

    @Test
    void validateVehicleRecovery_shouldIgnoreCaseForAccident() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("ACCIDENT")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validateVehicleRecovery(incident)
        );
    }


    // =========================
    // PENDING ALLOCATION
    // =========================

    @Test
    void validatePendingAllocation_shouldAllowMechanicalBreakdown() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Mechanical Breakdown")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validatePendingAllocation(incident)
        );
    }

    @Test
    void validatePendingAllocation_shouldAllowAccident() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Accident")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validatePendingAllocation(incident)
        );
    }

    @Test
    void validatePendingAllocation_shouldThrowExceptionForOtherIncidentType() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("Traffic Violation")
                                        .build()
                        )
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validatePendingAllocation(incident)
        );
    }

    @Test
    void validatePendingAllocation_shouldIgnoreCaseForMechanicalBreakdown() {

        Incident incident =
                Incident.builder()
                        .incidenttype(
                                IncidentType.builder()
                                        .name("mechanical breakdown")
                                        .build()
                        )
                        .build();

        assertDoesNotThrow(
                () -> validator.validatePendingAllocation(incident)
        );
    }


    // =========================
    // INITIAL STATUS
    // =========================

    @Test
    void validateInitialStatus_shouldAllowReportedStatus() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("REPORTED")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenInitialStatusIsInProgress() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("IN PROGRESS")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenInitialStatusIsResolved() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("RESOLVED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldThrowExceptionWhenInitialStatusIsClosed() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("CLOSED")
                        .build();

        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldIgnoreCase() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("reported")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateInitialStatus(status)
        );
    }

    @Test
    void validateInitialStatus_shouldIgnoreLeadingAndTrailingSpaces() {

        IncidentStatus status =
                IncidentStatus.builder()
                        .name("  REPORTED  ")
                        .build();

        assertDoesNotThrow(
                () -> validator.validateInitialStatus(status)
        );
    }
}