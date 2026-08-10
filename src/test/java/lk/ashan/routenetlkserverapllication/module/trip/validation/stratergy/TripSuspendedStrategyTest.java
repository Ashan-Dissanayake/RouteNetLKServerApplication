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

class TripSuspendedStrategyTest {

    private TripExecutionService tripExecutionService;
    private TripStatusService tripStatusService;
    private TripStateTransitionHandler tripStateTransitionHandler;

    private TripSuspendedStrategy strategy;

    @BeforeEach
    void setUp() {

        tripExecutionService = mock(TripExecutionService.class);
        tripStatusService = mock(TripStatusService.class);
        tripStateTransitionHandler = mock(TripStateTransitionHandler.class);

        strategy = new TripSuspendedStrategy(
                tripExecutionService,
                tripStatusService,
                tripStateTransitionHandler
        );
    }

    // -------------------------------------------------------------------------
    // IN PROGRESS trip
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldThrowException_WhenTripIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName()).thenReturn("IN PROGRESS");

        TripExecution execution = TripExecution.builder().tripexecutionstatus(executionStatus).build();

        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(List.of(execution));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.suspendTrip(trip)
        );

        assertEquals(
                "Cannot suspend the Master Schedule because there are trips currently 'IN PROGRESS' on the road.",
                exception.getMessage()
        );

        verify(tripExecutionService).getTripExecutionByTripId(1);

        verifyNoInteractions(tripStatusService);
        verifyNoInteractions(tripStateTransitionHandler);
    }

    // -------------------------------------------------------------------------
    // No IN PROGRESS trips
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldTransitionToSuspended_WhenNoTripsAreInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName()).thenReturn("COMPLETED");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();
        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(List.of(execution));

        Tripstatus suspendedStatus = mock(Tripstatus.class);
        when(tripStatusService.getByName("Suspended")).thenReturn(suspendedStatus);

        assertDoesNotThrow(() -> strategy.suspendTrip(trip));

        verify(tripExecutionService).getTripExecutionByTripId(1);
        verify(tripStatusService).getByName("Suspended");
        verify(tripStateTransitionHandler).transitionTo(trip, suspendedStatus);
    }

    // -------------------------------------------------------------------------
    // Empty execution list
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldTransitionToSuspended_WhenNoExecutionsExist() {

        Trip trip = Trip.builder()
                .id(1)
                .build();
        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(Collections.emptyList());

        Tripstatus suspendedStatus = mock(Tripstatus.class);
        when(tripStatusService.getByName("Suspended")).thenReturn(suspendedStatus);

        assertDoesNotThrow(() -> strategy.suspendTrip(trip));

        verify(tripExecutionService).getTripExecutionByTripId(1);
        verify(tripStatusService).getByName("Suspended");
        verify(tripStateTransitionHandler).transitionTo(trip, suspendedStatus);
    }

    // -------------------------------------------------------------------------
    // Other execution statuses should not block suspension
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldAllowSuspension_WhenExecutionIsCancelled() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus =
                mock(TripExecutionStatus.class);

        when(executionStatus.getName())
                .thenReturn("CANCELLED");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(execution));

        Tripstatus suspendedStatus =
                mock(Tripstatus.class);

        when(tripStatusService.getByName("Suspended"))
                .thenReturn(suspendedStatus);

        assertDoesNotThrow(
                () -> strategy.suspendTrip(trip)
        );

        verify(tripStatusService)
                .getByName("Suspended");

        verify(tripStateTransitionHandler)
                .transitionTo(trip, suspendedStatus);
    }

    // -------------------------------------------------------------------------
    // Multiple executions - one IN PROGRESS
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldThrowException_WhenOneOfMultipleExecutionsIsInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus completedStatus =
                mock(TripExecutionStatus.class);

        when(completedStatus.getName())
                .thenReturn("COMPLETED");

        TripExecutionStatus inProgressStatus =
                mock(TripExecutionStatus.class);

        when(inProgressStatus.getName())
                .thenReturn("IN PROGRESS");

        TripExecution completedExecution = TripExecution.builder()
                .tripexecutionstatus(completedStatus)
                .build();

        TripExecution inProgressExecution = TripExecution.builder()
                .tripexecutionstatus(inProgressStatus)
                .build();

        when(tripExecutionService.getTripExecutionByTripId(1))
                .thenReturn(List.of(
                        completedExecution,
                        inProgressExecution
                ));

        BusinessRuleViolationException exception = assertThrows(
                BusinessRuleViolationException.class,
                () -> strategy.suspendTrip(trip)
        );

        assertEquals(
                "Cannot suspend the Master Schedule because there are trips currently 'IN PROGRESS' on the road.",
                exception.getMessage()
        );

        verify(tripStatusService, never())
                .getByName(anyString());

        verify(tripStateTransitionHandler, never())
                .transitionTo(any(), any());
    }

    // -------------------------------------------------------------------------
    // Important: current implementation is case-sensitive
    // -------------------------------------------------------------------------

    @Test
    void suspendTrip_ShouldAllowSuspension_WhenStatusIsLowerCaseInProgress() {

        Trip trip = Trip.builder()
                .id(1)
                .build();

        TripExecutionStatus executionStatus = mock(TripExecutionStatus.class);
        when(executionStatus.getName()).thenReturn("in progress");

        TripExecution execution = TripExecution.builder()
                .tripexecutionstatus(executionStatus)
                .build();
        when(tripExecutionService.getTripExecutionByTripId(1)).thenReturn(List.of(execution));

        Tripstatus suspendedStatus = mock(Tripstatus.class);
        when(tripStatusService.getByName("Suspended")).thenReturn(suspendedStatus);


        assertDoesNotThrow(() -> strategy.suspendTrip(trip));

        verify(tripStatusService).getByName("Suspended");
        verify(tripStateTransitionHandler).transitionTo(trip, suspendedStatus);
    }
}

