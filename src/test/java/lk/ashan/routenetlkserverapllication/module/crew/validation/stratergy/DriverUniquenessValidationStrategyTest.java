package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.repository.DriverRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class DriverUniquenessValidationStrategyTest {

    @Mock
    private DriverRepository driverRepository;

    private DriverUniquenessValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new DriverUniquenessValidationStrategy(driverRepository);
    }

    @Test
    void validateCreate_ShouldThrowException_WhenLicenseNumberExists() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByLicensenumber("LN123")).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenEmployeeIdExists() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByEmployeeId(1)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenLicenseNumberExistsForAnotherDriver() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .id(1)
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByLicensenumberAndIdNot("LN123", 1)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenEmployeeIdExistsForAnotherDriver() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .id(1)
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByEmployeeIdAndIdNot(1, 1)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenAllFieldsAreUnique() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByLicensenumber("LN123")).thenReturn(false);
        when(driverRepository.existsByEmployeeId(1)).thenReturn(false);

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateUpdate_ShouldPass_WhenAllFieldsAreUnique() {
        // Arrange
        DriverValidationContext context = DriverValidationContext.builder()
                .id(1)
                .licenseNumber("LN123")
                .employeeId(1)
                .build();

        when(driverRepository.existsByLicensenumberAndIdNot("LN123", 1)).thenReturn(false);
        when(driverRepository.existsByEmployeeIdAndIdNot(1, 1)).thenReturn(false);

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
