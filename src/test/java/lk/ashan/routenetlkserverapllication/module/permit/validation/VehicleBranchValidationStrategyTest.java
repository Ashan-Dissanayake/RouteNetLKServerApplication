package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class VehicleBranchValidationStrategyTest {

    private VehicleRepository vehicleRepository;
    private VehicleBranchValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        vehicleRepository = Mockito.mock(VehicleRepository.class);
        validationStrategy = new VehicleBranchValidationStrategy(vehicleRepository);
    }

    @Test
    void validate_shouldThrowExceptionWhenVehicleDoesNotExist() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_shouldThrowExceptionWhenVehicleBranchDoesNotMatch() {
        // Arrange
        Branch branch = Branch.builder().id(2).build();
        Vehicle vehicle = Vehicle.builder().branch(branch).build();

        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .requestBranchId(3)
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_shouldPassWhenVehicleBranchMatches() {
        // Arrange
        Branch branch = Branch.builder()
                .id(1)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .branch(branch)
                .build();

        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .requestBranchId(1)
                .build();

        when(vehicleRepository.findById(1))
                .thenReturn(Optional.of(vehicle));

        // Act
        validationStrategy.validate(context);

        // Assert
        verify(vehicleRepository, times(1)).findById(1);
    }
}
