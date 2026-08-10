package lk.ashan.routenetlkserverapllication.module.incident.validation;


import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MechanicalIncidentStrategyTest {

    @Mock
    private TripExecutionRepository tripExecutionRepository;

    @InjectMocks
    private MechanicalIncidentStrategy mechanicalIncidentStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ShouldThrowException_WhenOdometerAtIncidentIsLessThanStartOdometer() {
        // Arrange
        TripExecution tripExecution = TripExecution.builder()
                .startodometer(1000)
                .build();

        IncidentContext context = IncidentContext.builder()
                .tripId(1)
                .odometerAtIncident(900)
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> mechanicalIncidentStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenOdometerAtIncidentIsValid() {
        // Arrange
        TripExecution tripExecution = TripExecution.builder()
                .startodometer(1000)
                .build();

        IncidentContext context = IncidentContext.builder()
                .tripId(1)
                .odometerAtIncident(1100)
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        mechanicalIncidentStrategy.validate(context);
    }

    @Test
    void isApplicable_ShouldReturnTrue_WhenTypeCodeIsMechanical() {
        // Act & Assert
        assert mechanicalIncidentStrategy.isApplicable("MECHANICAL");
    }

    @Test
    void isApplicable_ShouldReturnFalse_WhenTypeCodeIsNotMechanical() {
        // Act & Assert
        assert !mechanicalIncidentStrategy.isApplicable("OTHER");
    }
}
