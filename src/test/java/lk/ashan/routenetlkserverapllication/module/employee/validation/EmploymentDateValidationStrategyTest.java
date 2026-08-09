package lk.ashan.routenetlkserverapllication.module.employee.validation;


import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class EmploymentDateValidationStrategyTest {

    private final EmploymentDateValidationStrategy validationStrategy = new EmploymentDateValidationStrategy();

    @Test
    void validateCreate_ShouldThrowException_WhenProbationerHasOldDateOfJoining() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .employeeTypeName("probationers")
                .dateOfJoining(LocalDate.of(LocalDate.now().getYear() - 1, 1, 1))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenContractEmployeeHasOldDateOfJoining() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .employeeTypeName("contract")
                .dateOfJoining(LocalDate.of(LocalDate.now().getYear() - 1, 1, 1))
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenProbationerHasCurrentYearDateOfJoining() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .employeeTypeName("probationers")
                .dateOfJoining(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateCreate_ShouldPass_WhenPermanentEmployeeHasOldDateOfJoining() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .employeeTypeName("permanent")
                .dateOfJoining(LocalDate.of(LocalDate.now().getYear() - 5, 1, 1))
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }
}
