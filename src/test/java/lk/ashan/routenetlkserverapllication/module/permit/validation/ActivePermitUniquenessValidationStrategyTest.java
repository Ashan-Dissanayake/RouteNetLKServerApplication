package lk.ashan.routenetlkserverapllication.module.permit.validation;


import lk.ashan.routenetlkserverapllication.module.permit.repository.PermitRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ActivePermitUniquenessValidationStrategyTest {

    @Mock
    private PermitRepository permitRepository;

    @InjectMocks
    private ActivePermitUniquenessValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ShouldThrowException_WhenActivePermitExists() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        when(permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(true);

        // Act & Assert
        assertThrows(ResourceExistsException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenNoActivePermitExists() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(1)
                .routeId(2)
                .build();

        when(permitRepository.existsByVehicle_IdAndRoute_IdAndPermitestatus_Id(1, 2, 1))
                .thenReturn(false);

        // Act & Assert
        validationStrategy.validate(context);
    }

    @Test
    void validate_ShouldNotThrowException_WhenVehicleIdOrRouteIdIsNull() {
        // Arrange
        PermitValidationContext context = PermitValidationContext.builder()
                .vehicleId(null)
                .routeId(null)
                .build();

        // Act & Assert
        validationStrategy.validate(context);
    }
}
