package lk.ashan.routenetlkserverapllication.module.permit.validation;

import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Route;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.RouteType;
import lk.ashan.routenetlkserverapllication.module.permit.repository.RouteRepository;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.BusType;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.module.vehicle.repository.VehicleRepository;
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

class BusTypeRouteTypeValidationStrategyTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RouteRepository routeRepository;

    @InjectMocks
    private BusTypeRouteTypeValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ShouldThrowException_WhenVehicleDoesNotExist() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenRouteDoesNotExist() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        BusType vehicleType = new BusType();
        vehicleType.setName("AA");
        vehicle.setBustype(vehicleType);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(routeRepository.findById(2)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenCombinationIsInvalid() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        BusType vehicleType = new BusType();
        vehicleType.setName("E");
        vehicle.setBustype(vehicleType);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(routeRepository.findById(2)).thenReturn(Optional.of(Route.builder().routetype(RouteType.builder().name("inter provincial").build()).build()));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenCombinationIsValid() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        Vehicle vehicle = new Vehicle();
        BusType vehicleType = new BusType();
        vehicleType.setName("AA");
        vehicle.setBustype(vehicleType);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicle));
        when(routeRepository.findById(2)).thenReturn(Optional.of(Route.builder().routetype(RouteType.builder().name("intra provincial").build()).build()));

        // Act & Assert
        validationStrategy.validate(context);
    }
}
