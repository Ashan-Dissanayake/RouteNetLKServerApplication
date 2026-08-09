package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DriverImmutableFieldValidationStrategyTest {

    private DriverImmutableFieldValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        validationStrategy = new DriverImmutableFieldValidationStrategy();
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenLicenseNumberIsModified() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .existingLicenseNumber("ABC123")
                .licenseNumber("XYZ789")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenEmployeeIsReassigned() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .existingEmployeeId(1)
                .employeeId(2)
                .existingLicenseNumber("ABC123")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenLicenseIssuedDateIsModified() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .existingLicenseIssued(LocalDate.parse("2023-01-01"))
                .existingLicenseNumber("ABC123")
                .licenseIssued(LocalDate.parse("2023-02-01"))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldPass_WhenNoImmutableFieldsAreModified() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .existingLicenseNumber("ABC123")
                .licenseNumber("ABC123")
                .existingEmployeeId(1)
                .employeeId(1)
                .existingLicenseIssued(LocalDate.parse("2023-01-01"))
                .licenseIssued(LocalDate.parse("2023-01-01"))
                .build();

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
