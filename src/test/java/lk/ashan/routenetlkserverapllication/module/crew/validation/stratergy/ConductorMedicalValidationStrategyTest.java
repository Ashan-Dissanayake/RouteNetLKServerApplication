package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ConductorMedicalValidationStrategyTest {

    private ConductorMedicalValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        validationStrategy = new ConductorMedicalValidationStrategy();
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMedicalExpiryIsNotInFuture() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now().minusMonths(1))
                .medicalExpired(LocalDate.now().minusDays(1))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMedicalValidityExceedsSixMonths() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(7))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenMedicalDetailsAreValid() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(5))
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenMedicalExpiryIsNotInFuture() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now().minusMonths(1))
                .medicalExpired(LocalDate.now().minusDays(1))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenMedicalValidityExceedsSixMonths() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(7))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldPass_WhenMedicalDetailsAreValid() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .medicalIssued(LocalDate.now())
                .medicalExpired(LocalDate.now().plusMonths(5))
                .build();

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
