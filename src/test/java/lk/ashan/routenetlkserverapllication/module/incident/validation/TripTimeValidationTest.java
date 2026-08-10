package lk.ashan.routenetlkserverapllication.module.incident.validation;


import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TripTimeValidationTest {

    @Mock
    private TripExecutionRepository tripExecutionRepository;

    @InjectMocks
    private TripTimeValidation tripTimeValidation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validate_ShouldThrowException_WhenReportedTimeIsBeforeTripDeparture() {
        // Arrange
        TripExecution tripExecution = TripExecution.builder()
                .trip(Trip.builder()
                        .todepature(LocalTime.of(10, 0))
                        .toarrival(LocalTime.of(12, 0))
                        .build())
                .build();

        IncidentContext context = IncidentContext.builder()
                .tripId(1)
                .reportedTime(LocalTime.of(9, 0))
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> tripTimeValidation.validate(context));
    }

    @Test
    void validate_ShouldThrowException_WhenReportedTimeIsAfterTripArrival() {
        // Arrange
        TripExecution tripExecution = TripExecution.builder()
                .trip(Trip.builder()
                        .todepature(LocalTime.of(10, 0))
                        .toarrival(LocalTime.of(12, 0))
                        .build())
                .build();

        IncidentContext context = IncidentContext.builder()
                .tripId(1)
                .reportedTime(LocalTime.of(13, 0))
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        assertThrows(BusinessRuleViolationException.class, () -> tripTimeValidation.validate(context));
    }

    @Test
    void validate_ShouldNotThrowException_WhenReportedTimeIsWithinTripDuration() {
        // Arrange
        TripExecution tripExecution = TripExecution.builder()
                .trip(Trip.builder()
                        .todepature(LocalTime.of(10, 0))
                        .toarrival(LocalTime.of(12, 0))
                        .build())
                .build();

        IncidentContext context = IncidentContext.builder()
                .tripId(1)
                .reportedTime(LocalTime.of(11, 0))
                .build();

        when(tripExecutionRepository.findById(1)).thenReturn(Optional.of(tripExecution));

        // Act & Assert
        tripTimeValidation.validate(context);
    }

    @Test
    void isApplicable_ShouldAlwaysReturnTrue() {
        // Act & Assert
        assert tripTimeValidation.isApplicable("ANY_TYPE");
    }
}
