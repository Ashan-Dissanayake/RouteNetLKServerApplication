package lk.ashan.routenetlkserverapllication.module.trip.validation.stratergy;

import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Trip;
import lk.ashan.routenetlkserverapllication.module.trip.model.entity.Tripstatus;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
import lk.ashan.routenetlkserverapllication.module.trip.state.TripStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionService;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TripDiscontinuedStrategyTest {

    private TripExecutionService tripExecutionService;
    private TripStatusService tripStatusService;
    private TripStateTransitionHandler tripStateTransitionHandler;

    private TripDiscontinuedStrategy strategy;

    @BeforeEach
    void setUp() {

        tripExecutionService = mock(TripExecutionService.class);
        tripStatusService = mock(TripStatusService.class);
        tripStateTransitionHandler = mock(TripStateTransitionHandler.class);

        strategy = new TripDiscontinuedStrategy(
                tripExecutionService,
                tripStatusService,
                tripStateTransitionHandler
        );
    }

    // -------------------------------------------------------------------------
    // In Progress trip
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldThrowException_WhenTripIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName())
                .thenReturn("In Progress");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(execution));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.discontinueTrip(trip)
        );

        assertEquals(
                "Cannot discontinue: A vehicle is currently performing a journey for this route.",
                exception.getMessage()
        );

        verify(tripExecutionService)
                .getTripExecutionByTripId(1);

        verifyNoInteractions(tripStatusService);
        verifyNoInteractions(tripStateTransitionHandler);
    }

    // -------------------------------------------------------------------------
    // Completed trip
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldThrowException_WhenCompletedTripExists() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName())
                .thenReturn("Completed");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(execution));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.discontinueTrip(trip)
        );

        assertEquals(
                "Cannot discontinue: There are completed trips awaiting fare collection/settlement.",
                exception.getMessage()
        );

        verify(tripExecutionService)
                .getTripExecutionByTripId(1);

        verifyNoInteractions(tripStatusService);
        verifyNoInteractions(tripStateTransitionHandler);
    }

    // -------------------------------------------------------------------------
    // Valid case
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldTransitionToDiscontinued_WhenNoLiveOrCompletedTripsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName()).thenReturn("Cancelled");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();
        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(List.of(execution));

        Tripstatus discontinuedStatus = mock(Tripstatus.class);
        when(tripStatusService.getByName("Discontinued")).thenReturn(discontinuedStatus);

        assertDoesNotThrow(() -> strategy.discontinueTrip(trip));

        verify(tripExecutionService).getTripExecutionByTripId(1);
        verify(tripStatusService).getByName("Discontinued");
        verify(tripStateTransitionHandler).transitionTo(trip, discontinuedStatus);
    }

    // -------------------------------------------------------------------------
    // No executions
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldTransitionToDiscontinued_WhenNoExecutionsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(Collections.emptyList());

        Tripstatus discontinuedStatus = mock(Tripstatus.class);
        when(tripStatusService.getByName("Discontinued")).thenReturn(discontinuedStatus);

        assertDoesNotThrow(() -> strategy.discontinueTrip(trip));

        verify(tripExecutionService).getTripExecutionByTripId(1);
        verify(tripStatusService).getByName("Discontinued");
        verify(tripStateTransitionHandler).transitionTo(trip, discontinuedStatus);
    }

    // -------------------------------------------------------------------------
    // Case insensitive status names
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldThrowException_WhenInProgressStatusHasDifferentCase() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName()).thenReturn("IN PROGRESS");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();
        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(List.of(execution));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.discontinueTrip(trip)
        );

        assertEquals(
                "Cannot discontinue: A vehicle is currently performing a journey for this route.",
                exception.getMessage()
        );

        verifyNoInteractions(tripStatusService);
        verifyNoInteractions(tripStateTransitionHandler);
    }

    // -------------------------------------------------------------------------
    // In Progress takes priority over Completed
    // -------------------------------------------------------------------------

    @Test
    void discontinueTrip_ShouldCheckInProgressBeforeCompleted() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus inProgressStatus = mock(TripExecutionStatus.class);
        when(inProgressStatus.getName()).thenReturn("In Progress");

        TripExecutionStatus completedStatus = mock(TripExecutionStatus.class);
        when(completedStatus.getName()).thenReturn("Completed");

        TripExecution inProgressExecution = TripExecution.builder()
                .tripexecutionstatus(inProgressStatus)
                .build();

        TripExecution completedExecution = TripExecution.builder()
                .tripexecutionstatus(completedStatus)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        inProgressExecution,
                        completedExecution
                ));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.discontinueTrip(trip)
        );

        assertEquals(
                "Cannot discontinue: A vehicle is currently performing a journey for this route.",
                exception.getMessage()
        );

        verify(tripStatusService, never()).getByName(anyString());
        verify(tripStateTransitionHandler, never()).transitionTo(any(), any());
    }
}
