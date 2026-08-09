package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DriverLicenseMedicalValidationStrategyTest {

    private DriverLicenseMedicalValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        validationStrategy = new DriverLicenseMedicalValidationStrategy();
    }

    @Test
    void validateCreate_ShouldThrowException_WhenLicenseIssuedDateIsInFuture() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now().plusDays(1))
                .licenseExpired(LocalDate.now().plusYears(1))
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(6))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenLicenseExpiryIsBeforeIssuedDate() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now())
                .licenseExpired(LocalDate.now().minusDays(1))
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(6))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenLicenseValidityExceedsFourYears() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now())
                .licenseExpired(LocalDate.now().plusYears(5))
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(6))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMedicalIssuedDateIsInFuture() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now())
                .licenseExpired(LocalDate.now().plusYears(1))
                .medicalIssued(LocalDate.now().plusDays(1))
                .medicalExpired(LocalDate.now().plusMonths(6))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMedicalValidityExceedsSixMonths() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now())
                .licenseExpired(LocalDate.now().plusYears(1))
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(7))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenAllFieldsAreValid() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseIssued(LocalDate.now())
                .licenseExpired(LocalDate.now().plusYears(1))
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(6))
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }
}
