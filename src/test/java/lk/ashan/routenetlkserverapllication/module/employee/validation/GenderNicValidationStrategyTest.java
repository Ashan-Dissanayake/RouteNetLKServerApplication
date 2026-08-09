package lk.ashan.routenetlkserverapllication.module.employee.validation;


import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GenderNicValidationStrategyTest {

    private final GenderNicValidationStrategy validationStrategy = new GenderNicValidationStrategy();

    @Test
    void validateCreate_ShouldPass_WhenGenderMatchesNic() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("199012345678")
                .genderName("Male")
                .build();

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateCreate_ShouldThrowException_WhenGenderDoesNotMatchNic() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("199012345678")
                .genderName("Female")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateUpdate_ShouldPass_WhenGenderMatchesNic() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("199012345678")
                .genderName("Male")
                .build();

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenGenderDoesNotMatchNic() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("199012345678")
                .genderName("Female")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenNicIsNull() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic(null)
                .genderName("Male")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenNicIsInvalid() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("INVALID_NIC")
                .genderName("Male")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }
}
