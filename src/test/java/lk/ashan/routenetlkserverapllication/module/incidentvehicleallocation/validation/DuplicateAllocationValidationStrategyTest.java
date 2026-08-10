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

class DuplicateAllocationValidationStrategyTest {

    @Mock
    private IncidentVehicleAllocationRepository allocationRepository;

    private DuplicateAllocationValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new DuplicateAllocationValidationStrategy(allocationRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenDuplicateAllocationExists() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        when(allocationRepository.existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                1, 2, List.of("Assigned", "In progress"))).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenNoDuplicateAllocationExists() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        when(allocationRepository.existsByIncident_IdAndVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                1, 2, List.of("Assigned", "In progress"))).thenReturn(false);

        // Act & Assert
        validationStrategy.validate(context);
    }
}
