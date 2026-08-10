package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class IncidentAllocationLimitValidationStrategyTest {

    @Mock
    private IncidentVehicleAllocationRepository allocationRepository;

    private IncidentAllocationLimitValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new IncidentAllocationLimitValidationStrategy(allocationRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenActiveAllocationExists() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        when(allocationRepository.existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                1, List.of("Assigned", "In Progress"))).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenNoActiveAllocationExists() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        when(allocationRepository.existsByIncident_IdAndIncidentvehicleallocationstatus_NameIn(
                1, List.of("Assigned", "In Progress"))).thenReturn(false);

        // Act & Assert
        validationStrategy.validate(context);
    }
}
