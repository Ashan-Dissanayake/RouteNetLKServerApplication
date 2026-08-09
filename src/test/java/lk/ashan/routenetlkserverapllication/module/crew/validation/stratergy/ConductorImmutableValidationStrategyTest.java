package lk.ashan.routenetlkserverapllication.module.crew.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.Conductor;
import lk.ashan.routenetlkserverapllication.module.crew.repository.ConductorRepository;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ConductorImmutableValidationStrategyTest {

    @Mock
    private ConductorRepository conductorRepository;

    @InjectMocks
    private ConductorImmutableValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateUpdate_ShouldThrowResourceNotFoundException_WhenConductorNotFound() {
        // Arrange
        ConductorValidationContext context = ConductorValidationContext.builder()
                .id(1)
                .employeeId(100)
                .build();

        when(conductorRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldThrowBusinessRuleViolationException_WhenEmployeeIdIsChanged() {
        // Arrange
        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(Employee.builder().id(101).build())
                .build();

        ConductorValidationContext context = ConductorValidationContext.builder()
                .id(1)
                .employeeId(100)
                .build();

        when(conductorRepository.findById(1)).thenReturn(Optional.of(existingConductor));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validateUpdate(context));
    }

    @Test
    void validateUpdate_ShouldPass_WhenEmployeeIdIsUnchanged() {
        // Arrange
        Conductor existingConductor = Conductor.builder()
                .id(1)
                .employee(Employee.builder().id(100).build())
                .build();

        ConductorValidationContext context = ConductorValidationContext.builder()
                .id(1)
                .employeeId(100)
                .build();

        when(conductorRepository.findById(1)).thenReturn(Optional.of(existingConductor));

        // Act & Assert
        validationStrategy.validateUpdate(context);
    }
}
