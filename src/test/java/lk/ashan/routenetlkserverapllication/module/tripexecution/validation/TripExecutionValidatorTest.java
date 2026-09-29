package lk.ashan.routenetlkserverapllication.module.tripexecution.validation;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Driver;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.EmployeeStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TripExecutionValidatorTest {

    @InjectMocks
    private TripExecutionValidator tripExecutionValidator;

    // =========================================================
    // STATUS TRANSITION - SAME STATUS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusRemainsSame() {

        TripExecutionStatus current =
                status("SCHEDULED");

        TripExecutionStatus target =
                status("SCHEDULED");

        assertDoesNotThrow(() ->
                tripExecutionValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusNamesDifferOnlyByCase() {

        TripExecutionStatus current =
                status("scheduled");

        TripExecutionStatus target =
                status("SCHEDULED");

        assertDoesNotThrow(() ->
                tripExecutionValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenStatusNamesContainWhitespace() {

        TripExecutionStatus current =
                status("  SCHEDULED  ");

        TripExecutionStatus target =
                status(" SCHEDULED ");

        assertDoesNotThrow(() ->
                tripExecutionValidator.validateStatusTransition(
                        current,
                        target
                )
        );
    }

    // =========================================================
    // STATUS TRANSITION - SCHEDULED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenScheduledChangesToCheckedIn() {

        assertValidTransition(
                "SCHEDULED",
                "CHECKED IN"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenScheduledChangesToCancelled() {

        assertValidTransition(
                "SCHEDULED",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenScheduledChangesToDispatched() {

        assertInvalidTransition(
                "SCHEDULED",
                "DISPATCHED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenScheduledChangesToCompleted() {

        assertInvalidTransition(
                "SCHEDULED",
                "COMPLETED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - CHECKED IN
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenCheckedInChangesToDispatched() {

        assertValidTransition(
                "CHECKED IN",
                "DISPATCHED"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenCheckedInChangesToCancelled() {

        assertValidTransition(
                "CHECKED IN",
                "CANCELLED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCheckedInChangesToScheduled() {

        assertInvalidTransition(
                "CHECKED IN",
                "SCHEDULED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCheckedInChangesToArrived() {

        assertInvalidTransition(
                "CHECKED IN",
                "ARRIVED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - DISPATCHED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenDispatchedChangesToArrived() {

        assertValidTransition(
                "DISPATCHED",
                "ARRIVED"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenDispatchedChangesToBreakdown() {

        assertValidTransition(
                "DISPATCHED",
                "BREAKDOWN"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenDispatchedChangesToCompleted() {

        assertInvalidTransition(
                "DISPATCHED",
                "COMPLETED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenDispatchedChangesToCancelled() {

        assertInvalidTransition(
                "DISPATCHED",
                "CANCELLED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - ARRIVED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenArrivedChangesToCompleted() {

        assertValidTransition(
                "ARRIVED",
                "COMPLETED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenArrivedChangesToDispatched() {

        assertInvalidTransition(
                "ARRIVED",
                "DISPATCHED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenArrivedChangesToCancelled() {

        assertInvalidTransition(
                "ARRIVED",
                "CANCELLED"
        );
    }

    // =========================================================
    // STATUS TRANSITION - TERMINAL STATES
    // =========================================================

    @ParameterizedTest
    @ValueSource(strings = {
            "BREAKDOWN",
            "COMPLETED",
            "CANCELLED"
    })
    void validateStatusTransition_ShouldThrowException_WhenTerminalStatusChanges(
            String currentStatus
    ) {

        assertInvalidTransition(
                currentStatus,
                "SCHEDULED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenBreakdownChangesToCompleted() {

        assertInvalidTransition(
                "BREAKDOWN",
                "COMPLETED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCompletedChangesToScheduled() {

        assertInvalidTransition(
                "COMPLETED",
                "SCHEDULED"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCancelledChangesToCheckedIn() {

        assertInvalidTransition(
                "CANCELLED",
                "CHECKED IN"
        );
    }

    // =========================================================
    // STATUS TRANSITION - UNKNOWN STATUS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsUnknown() {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () -> tripExecutionValidator.validateStatusTransition(
                                status("UNKNOWN"),
                                status("SCHEDULED")
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Invalid trip execution status transition")
        );

        assertTrue(
                exception.getMessage()
                        .contains("UNKNOWN")
        );

        assertTrue(
                exception.getMessage()
                        .contains("SCHEDULED")
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenTargetStatusIsUnknown() {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () -> tripExecutionValidator.validateStatusTransition(
                                status("SCHEDULED"),
                                status("UNKNOWN")
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("UNKNOWN")
        );
    }

    // =========================================================
    // STATUS TRANSITION - CASE INSENSITIVITY
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenValidTransitionUsesDifferentCase() {

        assertValidTransition(
                "scheduled",
                "checked in"
        );
    }

    @Test
    void validateStatusTransition_ShouldThrowException_WhenInvalidTransitionUsesDifferentCase() {

        assertInvalidTransition(
                "scheduled",
                "dispatched"
        );
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenValidTransitionContainsWhitespace() {

        assertValidTransition(
                "  SCHEDULED  ",
                "  CHECKED IN  "
        );
    }

    // =========================================================
    // VALIDATE CHECK-IN - DRIVER
    // =========================================================

    @Test
    void validateCheckIn_ShouldThrowException_WhenDriverIsOnLeave() {

        TripExecution tripExecution =
                validTripExecution(
                        "On leave",
                        "Available"
                );

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> tripExecutionValidator.validateCheckIn(
                                tripExecution
                        )
                );

        assertEquals(
                "Driver is not Available on Today",
                exception.getMessage()
        );
    }

    // =========================================================
    // VALIDATE CHECK-IN - CONDUCTOR
    // =========================================================

    @Test
    void validateCheckIn_ShouldThrowException_WhenConductorIsOnLeave() {

        TripExecution tripExecution =
                validTripExecution(
                        "Available",
                        "On leave"
                );

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> tripExecutionValidator.validateCheckIn(
                                tripExecution
                        )
                );

        assertEquals(
                "Conductor is not Available on Today",
                exception.getMessage()
        );
    }

    // =========================================================
    // VALIDATE CHECK-IN - BOTH AVAILABLE
    // =========================================================

    @Test
    void validateCheckIn_ShouldPass_WhenDriverAndConductorAreAvailable() {

        TripExecution tripExecution =
                validTripExecution(
                        "Available",
                        "Available"
                );

        assertDoesNotThrow(() ->
                tripExecutionValidator.validateCheckIn(
                        tripExecution
                )
        );
    }

    // =========================================================
    // VALIDATE CHECK-IN - CASE INSENSITIVITY
    // =========================================================

    @Test
    void validateCheckIn_ShouldThrowException_WhenDriverStatusIsOnLeaveIgnoringCase() {

        TripExecution tripExecution =
                validTripExecution(
                        "ON LEAVE",
                        "Available"
                );

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> tripExecutionValidator.validateCheckIn(
                                tripExecution
                        )
                );

        assertEquals(
                "Driver is not Available on Today",
                exception.getMessage()
        );
    }

    @Test
    void validateCheckIn_ShouldThrowException_WhenConductorStatusIsOnLeaveIgnoringCase() {

        TripExecution tripExecution =
                validTripExecution(
                        "Available",
                        "on leave"
                );

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> tripExecutionValidator.validateCheckIn(
                                tripExecution
                        )
                );

        assertEquals(
                "Conductor is not Available on Today",
                exception.getMessage()
        );
    }

    // =========================================================
    // VALIDATE CHECK-IN - PRIORITY
    // =========================================================

    @Test
    void validateCheckIn_ShouldReportDriver_WhenBothDriverAndConductorAreOnLeave() {

        TripExecution tripExecution =
                validTripExecution(
                        "On leave",
                        "On leave"
                );

        BusinessRuleViolationException exception =
                assertThrows(
                        BusinessRuleViolationException.class,
                        () -> tripExecutionValidator.validateCheckIn(
                                tripExecution
                        )
                );

        assertEquals(
                "Driver is not Available on Today",
                exception.getMessage()
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private TripExecutionStatus status(String name) {

        return TripExecutionStatus.builder()
                .name(name)
                .build();
    }

    private void assertValidTransition(
            String currentStatus,
            String targetStatus
    ) {

        assertDoesNotThrow(() ->
                tripExecutionValidator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    private void assertInvalidTransition(
            String currentStatus,
            String targetStatus
    ) {

        InvalidStateTransitionException exception =
                assertThrows(
                        InvalidStateTransitionException.class,
                        () -> tripExecutionValidator.validateStatusTransition(
                                status(currentStatus),
                                status(targetStatus)
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("Invalid trip execution status transition")
        );

        assertTrue(
                exception.getMessage()
                        .contains(currentStatus.trim().toUpperCase())
        );

        assertTrue(
                exception.getMessage()
                        .contains(targetStatus.trim().toUpperCase())
        );
    }

    private TripExecution validTripExecution(
            String driverStatus,
            String conductorStatus
    ) {

        Employee driverEmployee =
                Employee.builder()
                        .employeestatus(
                                employeeStatus(driverStatus)
                        )
                        .build();

        Employee conductorEmployee =
                Employee.builder()
                        .employeestatus(
                                employeeStatus(conductorStatus)
                        )
                        .build();

        // Use the actual builder methods/names from your entity
        // if TripExecution uses a different relationship structure.
        return TripExecution.builder()
                .driver(
                        Driver.builder()
                                .employee(driverEmployee)
                                .build()
                )
                .conductor(
                        Conductor.builder()
                                .employee(conductorEmployee)
                                .build()
                )
                .build();
    }

    private EmployeeStatus employeeStatus(String name) {

        return EmployeeStatus.builder()
                .name(name)
                .build();
    }
}