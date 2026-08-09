package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ConductorUniquenessValidationStrategyTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private ConductorUniquenessValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateCreate_ShouldThrowException_WhenCrewStatusIsNotEligible() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .crewStatus("Ineligible")
                .routeFamiliarityLevel("Low")
                .employeeId(100)
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenRouteFamiliarityIsNotLow() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .crewStatus("Eligible")
                .routeFamiliarityLevel("High")
                .employeeId(100)
                .build();

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateCreate_ShouldThrowException_WhenEmployeeAlreadyExists() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .crewStatus("Eligible")
                .routeFamiliarityLevel("Low")
                .employeeId(100)
                .build();

        when(conductorRepository.existsByEmployeeId(100)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateCreate(context));
    }

    @Test
    void validateUpdate_ShouldThrowException_WhenEmployeeAlreadyExistsForAnotherId() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .id(1)
                .employeeId(100)
                .build();

        when(conductorRepository.existsByEmployeeIdAndIdNot(100, 1)).thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateCreate_ShouldPass_WhenAllConditionsAreMet() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .crewStatus("Eligible")
                .routeFamiliarityLevel("Low")
                .employeeId(100)
                .build();

        when(conductorRepository.existsByEmployeeId(100)).thenReturn(false);

        // Act & Assert
        validationStrategy.validateCreate(context);
    }

    @Test
    void validateUpdate_ShouldPass_WhenNoConflictsExist() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .id(1)
                .employeeId(100)
                .build();

        when(conductorRepository.existsByEmployeeIdAndIdNot(100, 1)).thenReturn(false);

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
