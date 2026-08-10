package lk.ashan.routenetlkserverapllication.module.permit.validation;


import lk.ashan.routenetlkserverapllication.module.permit.model.entity.ServiceType;
import lk.ashan.routenetlkserverapllication.module.permit.repository.ServiceTypeRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BusTypeServiceTypeValidationStrategyTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ServiceTypeRepository serviceTypeRepository;

    private BusTypeServiceTypeValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new BusTypeServiceTypeValidationStrategy(vehicleRepository, serviceTypeRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenVehicleNotFound() {
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .serviceTypeId(1)
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
        verify(vehicleRepository, times(1)).findById(1);
    }

    @Test
    void validate_ShouldThrowException_WhenServiceTypeNotFound() {
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .serviceTypeId(1)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .bustype(BusType.builder().name("AA").build())
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(serviceTypeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> validationStrategy.validate(context));
        verify(vehicleRepository, times(1)).findById(1);
        verify(serviceTypeRepository, times(1)).findById(1);
    }

    @ParameterizedTest
    @CsvSource({
            "AA, luxury, true",
            "A, normal, true",
            "B, semi luxury, true",
            "AA, normal, false"
    })
    void validate_ShouldValidateCombinations(String busType, String serviceType, boolean isValid) {
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .serviceTypeId(1)
                .build();

        Vehicle vehicle = Vehicle.builder()
                .bustype(BusType.builder().name(busType).build())
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(serviceTypeRepository.findById(1)).thenReturn(Optional.of(
                ServiceType.builder().name(serviceType).build()
        ));

        if (isValid) {
            validationStrategy.validate(context);
        } else {
            assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
        }

        verify(vehicleRepository, times(1)).findById(1);
        verify(serviceTypeRepository, times(1)).findById(1);
    }
}
