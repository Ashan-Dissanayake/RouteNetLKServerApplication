package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.repository.IncidentVehicleAllocationRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class VehicleAvailabilityValidationStrategyTest {

    @Mock
    private IncidentVehicleAllocationRepository allocationRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    private VehicleAvailabilityValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new VehicleAvailabilityValidationStrategy(allocationRepository, vehicleRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenVehicleIsNotAvailable() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        VehicleStatus status = new VehicleStatus();
        status.setName("In Use");
        vehicle.setVehiclestatus(status);

        when(vehicleRepository.findById(2)).thenReturn(Optional.of(vehicle));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenVehicleIsAlreadyAllocated() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        VehicleStatus status = new VehicleStatus();
        status.setName("Available");
        vehicle.setVehiclestatus(status);

        when(vehicleRepository.findById(2)).thenReturn(Optional.of(vehicle));
        when(allocationRepository.existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                2, List.of("Assigned", "In Progress"))).thenReturn(true);

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenVehicleIsAvailableAndNotAllocated() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        VehicleStatus status = new VehicleStatus();
        status.setName("Available");
        vehicle.setVehiclestatus(status);

        when(vehicleRepository.findById(2)).thenReturn(Optional.of(vehicle));
        when(allocationRepository.existsByVehicle_IdAndIncidentvehicleallocationstatus_NameIn(
                2, List.of("Assigned", "In Progress"))).thenReturn(false);

        // Act & Assert
        validationStrategy.validate(context);
    }
}
