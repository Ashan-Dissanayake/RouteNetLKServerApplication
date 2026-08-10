package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.validation;

import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class IncidentStateValidationStrategyTest {

    @Mock
    private IncidentRepository incidentRepository;

    private IncidentStateValidationStrategy validationStrategy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validationStrategy = new IncidentStateValidationStrategy(incidentRepository);
    }

    @Test
    void validate_ShouldThrowException_WhenIncidentIsInTerminalState() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        Incident incident = new Incident();
        IncidentStatus status = new IncidentStatus();
        status.setName("Closed");
        incident.setIncidentstatus(status);

        when(incidentRepository.findById(1)).thenReturn(Optional.of(incident));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenIncidentIsNotInTerminalState() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        Incident incident = new Incident();
        IncidentStatus status = new IncidentStatus();
        status.setName("Open");
        incident.setIncidentstatus(status);

        when(incidentRepository.findById(1)).thenReturn(Optional.of(incident));

        // Act & Assert
        validationStrategy.validate(context);
    }

    @Test
    void validate_ShouldThrowException_WhenIncidentNotFound() {
        // Arrange
        AllocationContext context = AllocationContext.builder()
                .incidentId(1)
                .vehicleId(2)
                .build();

        when(incidentRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> validationStrategy.validate(context));
    }
}
