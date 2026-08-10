package lk.ashan.routenetlkserverapllication.module.vehicleservice.validation;


import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.VehicleStatus;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePartDto;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceCreationValidationStrategyTest {

    private VehicleRepository vehicleRepository;
    private IncidentRepository incidentRepository;

    private VehicleServiceCreationValidationStrategy strategy;

    @BeforeEach
    void setUp() {

        vehicleRepository = mock(VehicleRepository.class);
        incidentRepository = mock(IncidentRepository.class);

        strategy = new VehicleServiceCreationValidationStrategy(
                vehicleRepository,
                incidentRepository
        );
    }

    // -------------------------------------------------------------------------
    // Part quantity validation
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldThrowException_WhenPartQuantityIsNull() {

        VehicleServicePartDto part = mock(VehicleServicePartDto.class);
        when(part.getQuantity()).thenReturn(null);

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(List.of(part))
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenPartQuantityIsZero() {

        VehicleServicePartDto part = mock(VehicleServicePartDto.class);
        when(part.getQuantity()).thenReturn(BigDecimal.ZERO);

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(List.of(part))
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenPartQuantityIsNegative() {

        VehicleServicePartDto part = mock(VehicleServicePartDto.class);
        when(part.getQuantity()).thenReturn(new BigDecimal("-1"));

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(List.of(part))
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldPass_WhenPartQuantityIsPositive() {

        VehicleServicePartDto part = mock(VehicleServicePartDto.class);
        when(part.getQuantity()).thenReturn(new BigDecimal("2"));

        mockNormalVehicle();

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(List.of(part))
                        .build();

        assertDoesNotThrow(
                () -> strategy.validate(context)
        );
    }

    // -------------------------------------------------------------------------
    // Null parts
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldPass_WhenPartsAreNull() {

        mockNormalVehicle();

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(null)
                        .build();

        assertDoesNotThrow(
                () -> strategy.validate(context)
        );

        verify(vehicleRepository).findById(1);
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldPass_WhenPartsAreEmpty() {

        mockNormalVehicle();

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(Collections.emptyList())
                        .build();

        assertDoesNotThrow(() -> strategy.validate(context));

        verify(vehicleRepository).findById(1);
        verifyNoInteractions(incidentRepository);
    }

    // -------------------------------------------------------------------------
    // Breakdown repair - incident validation
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldThrowException_WhenBreakdownRepairHasNoIncidentId() {

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("BREAKDOWN_REPAIR")
                        .incidentId(null)
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "An active incident ID must be attached for breakdown repairs",
                exception.getMessage()
        );

        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenBreakdownRepairIncidentDoesNotExist() {

        when(incidentRepository.findById(10)).thenReturn(Optional.empty());

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("BREAKDOWN_REPAIR")
                        .incidentId(10)
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Attached incident target not found",
                exception.getMessage()
        );

        verify(incidentRepository).findById(10);
        verifyNoInteractions(vehicleRepository);
    }

    @Test
    void validate_ShouldContinue_WhenBreakdownRepairIncidentExists() {

        Incident incident = mock(Incident.class);
        when(incidentRepository.findById(10)).thenReturn(Optional.of(incident));

        mockNormalVehicle();

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("BREAKDOWN_REPAIR")
                        .incidentId(10)
                        .build();

        assertDoesNotThrow(() -> strategy.validate(context));

        verify(incidentRepository).findById(10);
        verify(vehicleRepository).findById(1);
    }

    // -------------------------------------------------------------------------
    // Breakdown repair - case insensitive
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldTreatBreakdownRepairCaseInsensitively() {

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("breakdown_repair")
                        .incidentId(null)
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "An active incident ID must be attached for breakdown repairs",
                exception.getMessage()
        );

        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    // -------------------------------------------------------------------------
    // Vehicle existence
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldThrowException_WhenVehicleDoesNotExist() {

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("ROUTINE_SERVICE")
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Vehicle target not found",
                exception.getMessage()
        );

        verify(vehicleRepository).findById(1);
        verifyNoInteractions(incidentRepository);
    }

    // -------------------------------------------------------------------------
    // Vehicle maintenance status
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldThrowException_WhenVehicleIsAlreadyUnderMaintenance() {

        VehicleStatus vehicleStatus = mock(VehicleStatus.class);
        when(vehicleStatus.getName()).thenReturn("UNDER_MAINTENANCE");

        Vehicle vehicle = mock(Vehicle.class);
        when(vehicle.getVehiclestatus()).thenReturn(vehicleStatus);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("ROUTINE_SERVICE")
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "This vehicle is already booked into a maintenance loop",
                exception.getMessage()
        );

        verify(vehicleRepository).findById(1);
        verify(vehicleStatus).getName();
        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldTreatVehicleMaintenanceStatusCaseInsensitively() {

        VehicleStatus vehicleStatus = mock(VehicleStatus.class);
        when(vehicleStatus.getName()).thenReturn("under_maintenance");

        Vehicle vehicle = mock(Vehicle.class);
        when(vehicle.getVehiclestatus()).thenReturn(vehicleStatus);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("ROUTINE_SERVICE")
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "This vehicle is already booked into a maintenance loop",
                exception.getMessage()
        );
    }

    // -------------------------------------------------------------------------
    // Valid scenarios
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldPass_WhenVehicleIsAvailableForMaintenance() {

        mockNormalVehicle();

        VehicleServicePartDto part = mock(VehicleServicePartDto.class);

        when(part.getQuantity())
                .thenReturn(new BigDecimal("5"));

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("ROUTINE_SERVICE")
                        .parts(List.of(part))
                        .incidentId(null)
                        .build();

        assertDoesNotThrow(
                () -> strategy.validate(context)
        );

        verify(vehicleRepository)
                .findById(1);

        verifyNoInteractions(incidentRepository);
    }

    @Test
    void validate_ShouldPass_WhenNonBreakdownRepairHasNoIncident() {

        mockNormalVehicle();

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .serviceTypeName("ROUTINE_SERVICE")
                        .incidentId(null)
                        .build();

        assertDoesNotThrow(
                () -> strategy.validate(context)
        );

        verify(vehicleRepository)
                .findById(1);

        verifyNoInteractions(incidentRepository);
    }

    // -------------------------------------------------------------------------
    // Multiple parts
    // -------------------------------------------------------------------------

    @Test
    void validate_ShouldThrowException_WhenAnyPartHasInvalidQuantity() {

        VehicleServicePartDto validPart = mock(VehicleServicePartDto.class);
        when(validPart.getQuantity()).thenReturn(new BigDecimal("2"));

        VehicleServicePartDto invalidPart = mock(VehicleServicePartDto.class);
        when(invalidPart.getQuantity()).thenReturn(BigDecimal.ZERO);

        VehicleServiceValidationContext context =
                validContextBuilder()
                        .parts(List.of(validPart, invalidPart))
                        .build();

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.validate(context)
        );

        assertEquals(
                "Requested part quantity must be greater than zero",
                exception.getMessage()
        );

        /*
         * Quantity validation happens before incident/vehicle validation.
         */
        verifyNoInteractions(vehicleRepository);
        verifyNoInteractions(incidentRepository);
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------

    private VehicleServiceValidationContext.VehicleServiceValidationContextBuilder
    validContextBuilder() {

        return VehicleServiceValidationContext.builder()
                .branchId(1)
                .vehicleId(1)
                .serviceTypeName("ROUTINE_SERVICE")
                .incidentId(null)
                .parts(Collections.emptyList());
    }

    private void mockNormalVehicle() {

        VehicleStatus vehicleStatus = mock(VehicleStatus.class);
        when(vehicleStatus.getName()).thenReturn("AVAILABLE");

        Vehicle vehicle = mock(Vehicle.class);
        when(vehicle.getVehiclestatus()).thenReturn(vehicleStatus);
        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
    }
}
