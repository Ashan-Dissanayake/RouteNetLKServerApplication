package lk.ashan.routenetlkserverapllication.module.employee.validation;


import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class DesignationRulesValidationStrategyTest {

    private DesignationRulesValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        validationStrategy = new DesignationRulesValidationStrategy();
    }

    @Test
    void validateCreate_ShouldThrowException_WhenInvalidDepartmentDesignationCombination() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .departmentName("invalid department")
                .designationName("driver")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenFemaleEmployeeIsDriver() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .genderName("female")
                .designationName("driver")
                .departmentName("Operation")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenInvalidDepartmentDesignationCombination() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .departmentName("invalid department")
                .designationName("driver")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenFemaleEmployeeIsDriver() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .genderName("female")
                .designationName("driver")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenValidDepartmentDesignationCombination() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .departmentName("operations")
                .designationName("driver")
                .genderName("male")
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateUpdate_ShouldPass_WhenValidDepartmentDesignationCombination() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .departmentName("engineering and technical")
                .designationName("mechanic")
                .genderName("male")
                .build();

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
