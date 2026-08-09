package lk.ashan.routenetlkserverapllication.module.employee.validation;

import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmployeeUniquenessValidationStrategyTest {

    private EmployeeRepository employeeRepository;
    private EmployeeUniquenessValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        validationStrategy = new EmployeeUniquenessValidationStrategy(employeeRepository);
    }

    @Test
    void validateCreate_ShouldThrowException_WhenNicAlreadyExists() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByNic("123456789V")).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMobileAlreadyExists() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByMobile("0771234567")).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenEmergencyContactAlreadyExists() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByEmergencycontact("0777654321")).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMobileUsedAsEmergencyContact() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByEmergencycontact("0771234567")).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenEmergencyContactUsedAsMobile() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByMobile("0777654321")).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenMobileAndEmergencyContactAreSame() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0771234567")
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenAllFieldsAreUnique() {
        // Arrange
        EmployeeValidationContext context = EmployeeValidationContext.builder()
                .nic("123456789V")
                .mobile("0771234567")
                .emergencyContact("0777654321")
                .build();

        when(employeeRepository.existsByNic(anyString())).thenReturn(false);
        when(employeeRepository.existsByMobile(anyString())).thenReturn(false);
        when(employeeRepository.existsByEmergencycontact(anyString())).thenReturn(false);

        // Act & Assert
        validationStrategy.validateCreate(context);
    }
}
