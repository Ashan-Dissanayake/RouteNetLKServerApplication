package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RosterShiftAssignmentValidatorTest {

    @InjectMocks
    private RosterShiftAssignmentValidator validator;

    // =========================================================
    // SAME STATUS
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "DRAFT",
            "PROPOSED",
            "CONFIRMED",
            "IN-PROGRESS",
            "COMPLETED",
            "CANCELED",
            "ABSENT"
    })
    void validateStatusTransition_ShouldPass_WhenStatusRemainsUnchanged(
            String statusName
    ) {
        // Arrange
        RosterShiftAssignmentStatus currentStatus = status(statusName);
        RosterShiftAssignmentStatus targetStatus = status(statusName);

        // Act & Assert
        assertDoesNotThrow(() ->
                validator.validateStatusTransition(
                        currentStatus,
                        targetStatus
                )
        );
    }

    // =========================================================
    // VALID TRANSITIONS - DRAFT
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenDraftChangesToProposed() {
        assertValidTransition("DRAFT", "PROPOSED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenDraftChangesToCanceled() {
        assertValidTransition("DRAFT", "CANCELED");
    }

    // =========================================================
    // VALID TRANSITIONS - PROPOSED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenProposedChangesToConfirmed() {
        assertValidTransition("PROPOSED", "CONFIRMED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenProposedChangesToCanceled() {
        assertValidTransition("PROPOSED", "CANCELED");
    }

    // =========================================================
    // VALID TRANSITIONS - CONFIRMED
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenConfirmedChangesToInProgress() {
        assertValidTransition("CONFIRMED", "IN-PROGRESS");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenConfirmedChangesToCanceled() {
        assertValidTransition("CONFIRMED", "CANCELED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenConfirmedChangesToAbsent() {
        assertValidTransition("CONFIRMED", "ABSENT");
    }

    // =========================================================
    // VALID TRANSITIONS - IN-PROGRESS
    // =========================================================

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToCompleted() {
        assertValidTransition("IN-PROGRESS", "COMPLETED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToCanceled() {
        assertValidTransition("IN-PROGRESS", "CANCELED");
    }

    @Test
    void validateStatusTransition_ShouldPass_WhenInProgressChangesToAbsent() {
        assertValidTransition("IN-PROGRESS", "ABSENT");
    }

    // =========================================================
    // INVALID TRANSITIONS - DRAFT
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "DRAFT, CONFIRMED",
            "DRAFT, IN-PROGRESS",
            "DRAFT, COMPLETED",
            "DRAFT, ABSENT"
    })
    void validateStatusTransition_ShouldThrowException_WhenDraftTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // INVALID TRANSITIONS - PROPOSED
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "PROPOSED, DRAFT",
            "PROPOSED, IN-PROGRESS",
            "PROPOSED, COMPLETED",
            "PROPOSED, ABSENT"
    })
    void validateStatusTransition_ShouldThrowException_WhenProposedTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // INVALID TRANSITIONS - CONFIRMED
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "CONFIRMED, DRAFT",
            "CONFIRMED, PROPOSED",
            "CONFIRMED, COMPLETED"
    })
    void validateStatusTransition_ShouldThrowException_WhenConfirmedTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // INVALID TRANSITIONS - IN-PROGRESS
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "IN-PROGRESS, DRAFT",
            "IN-PROGRESS, PROPOSED",
            "IN-PROGRESS, CONFIRMED"
    })
    void validateStatusTransition_ShouldThrowException_WhenInProgressTransitionIsInvalid(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // TERMINAL STATES
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "COMPLETED, DRAFT",
            "COMPLETED, PROPOSED",
            "COMPLETED, CONFIRMED",
            "COMPLETED, IN-PROGRESS",
            "COMPLETED, CANCELED",
            "COMPLETED, ABSENT",

            "CANCELED, DRAFT",
            "CANCELED, PROPOSED",
            "CANCELED, CONFIRMED",
            "CANCELED, IN-PROGRESS",
            "CANCELED, COMPLETED",
            "CANCELED, ABSENT",

            "ABSENT, DRAFT",
            "ABSENT, PROPOSED",
            "ABSENT, CONFIRMED",
            "ABSENT, IN-PROGRESS",
            "ABSENT, COMPLETED",
            "ABSENT, CANCELED"
    })
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsTerminal(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // UNKNOWN STATUS
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "UNKNOWN, DRAFT",
            "UNKNOWN, PROPOSED",
            "UNKNOWN, CONFIRMED",
            "UNKNOWN, IN-PROGRESS",
            "UNKNOWN, COMPLETED",
            "UNKNOWN, CANCELED",
            "UNKNOWN, ABSENT"
    })
    void validateStatusTransition_ShouldThrowException_WhenCurrentStatusIsUnknown(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    @ParameterizedTest
    @CsvSource({
            "DRAFT, UNKNOWN",
            "PROPOSED, UNKNOWN",
            "CONFIRMED, UNKNOWN",
            "IN-PROGRESS, UNKNOWN"
    })
    void validateStatusTransition_ShouldThrowException_WhenTargetStatusIsUnknown(
            String currentStatus,
            String targetStatus
    ) {
        assertInvalidTransition(currentStatus, targetStatus);
    }

    // =========================================================
    // CASE INSENSITIVITY
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "draft, proposed",
            "PROPOSED, confirmed",
            "confirmed, in-progress",
            "IN-PROGRESS, completed",
            "confirmed, absent",
            "DRAFT, canceled"
    })
    void validateStatusTransition_ShouldPass_WhenStatusNamesHaveDifferentCase(
            String currentStatus,
            String targetStatus
    ) {
        // Act & Assert
        assertDoesNotThrow(() ->
                validator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    // =========================================================
    // WHITESPACE NORMALIZATION
    // =========================================================

    @ParameterizedTest
    @CsvSource({
            "'  DRAFT  ', '  PROPOSED  '",
            "' PROPOSED ', ' CONFIRMED '",
            "'  CONFIRMED  ', ' IN-PROGRESS '",
            "' IN-PROGRESS ', ' COMPLETED '"
    })
    void validateStatusTransition_ShouldPass_WhenStatusNamesContainWhitespace(
            String currentStatus,
            String targetStatus
    ) {
        // Act & Assert
        assertDoesNotThrow(() ->
                validator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    // =========================================================
    // NORMALIZATION + INVALID TRANSITION
    // =========================================================

    @Test
    void validateStatusTransition_ShouldThrowException_WhenNormalizedTransitionIsInvalid() {
        // Arrange
        RosterShiftAssignmentStatus currentStatus = status("  draft  ");
        RosterShiftAssignmentStatus targetStatus = status("  confirmed  ");

        // Act & Assert
        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        currentStatus,
                        targetStatus
                )
        );
    }

    // =========================================================
    // HELPERS
    // =========================================================

    private RosterShiftAssignmentStatus status(String name) {
        return RosterShiftAssignmentStatus.builder()
                .name(name)
                .build();
    }

    private void assertValidTransition(
            String currentStatus,
            String targetStatus
    ) {
        assertDoesNotThrow(() ->
                validator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }

    private void assertInvalidTransition(
            String currentStatus,
            String targetStatus
    ) {
        assertThrows(
                BusinessRuleViolationException.class,
                () -> validator.validateStatusTransition(
                        status(currentStatus),
                        status(targetStatus)
                )
        );
    }
}