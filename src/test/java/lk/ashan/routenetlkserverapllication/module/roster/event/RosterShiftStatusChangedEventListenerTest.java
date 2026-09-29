package lk.ashan.routenetlkserverapllication.module.roster.event;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.module.crew.service.CrewStatusService;
import lk.ashan.routenetlkserverapllication.module.crew.service.DriverService;
import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RosterShiftStatusChangedEventListenerTest {

    @Mock
    private DriverService driverService;

    @Mock
    private ConductorService conductorService;

    @Mock
    private CrewStatusService crewStatusService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private RosterShiftStatusChangedEventListener listener;

    private CrewStatus activeStatus;
    private CrewStatus inactiveStatus;

    @BeforeEach
    void setUp() {
        activeStatus = new CrewStatus();
        activeStatus.setName("ACTIVE");

        inactiveStatus = new CrewStatus();
        inactiveStatus.setName("INACTIVE");
    }

    @Test
    void shouldActivateDriverWhenAssignmentIsConfirmed() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        1,
                        100,
                        10,
                        "DRIVER",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("ACTIVE");

        verify(driverService)
                .updateCrewStatus(100, activeStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(conductorService);
    }

    @Test
    void shouldActivateConductorWhenAssignmentIsConfirmed() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        1,
                        100,
                        10,
                        "CONDUCTOR",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("ACTIVE");

        verify(conductorService)
                .updateCrewStatus(100, activeStatus);

        verifyNoInteractions(driverService);

        verify(notificationService)
                .createRosterStatusNotification(event);
    }

    @Test
    void shouldActivateDriverWhenAssignmentIsInProgress() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        2,
                        101,
                        10,
                        "DRIVER",
                        "CONFIRMED",
                        "IN-PROGRESS"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("ACTIVE");

        verify(driverService)
                .updateCrewStatus(101, activeStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(conductorService);
    }

    @Test
    void shouldDeactivateDriverWhenAssignmentIsCompleted() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        3,
                        102,
                        10,
                        "DRIVER",
                        "IN-PROGRESS",
                        "COMPLETED"
                );

        when(crewStatusService.getByName("INACTIVE"))
                .thenReturn(inactiveStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("INACTIVE");

        verify(driverService)
                .updateCrewStatus(102, inactiveStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(conductorService);
    }

    @Test
    void shouldDeactivateConductorWhenAssignmentIsCanceled() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        4,
                        103,
                        10,
                        "CONDUCTOR",
                        "CONFIRMED",
                        "CANCELED"
                );

        when(crewStatusService.getByName("INACTIVE"))
                .thenReturn(inactiveStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("INACTIVE");

        verify(conductorService)
                .updateCrewStatus(103, inactiveStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(driverService);
    }

    @Test
    void shouldDeactivateDriverWhenAssignmentIsAbsent() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        5,
                        104,
                        10,
                        "DRIVER",
                        "IN-PROGRESS",
                        "ABSENT"
                );

        when(crewStatusService.getByName("INACTIVE"))
                .thenReturn(inactiveStatus);

        listener.handle(event);

        verify(crewStatusService).getByName("INACTIVE");

        verify(driverService)
                .updateCrewStatus(104, inactiveStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(conductorService);
    }

    @Test
    void shouldStillSendNotificationWhenCrewTypeIsUnknown() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        6,
                        105,
                        10,
                        "UNKNOWN",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        listener.handle(event);

        verify(notificationService)
                .createRosterStatusNotification(event);

        verifyNoInteractions(driverService);
        verifyNoInteractions(conductorService);
    }

    @Test
    void shouldStillSendNotificationWhenCrewUpdateFails() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        7,
                        106,
                        10,
                        "DRIVER",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        doThrow(new RuntimeException("Crew update failed"))
                .when(driverService)
                .updateCrewStatus(106, activeStatus);

        listener.handle(event);

        verify(driverService)
                .updateCrewStatus(106, activeStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);
    }

    @Test
    void shouldNotAffectCrewUpdateWhenNotificationFails() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        8,
                        107,
                        10,
                        "DRIVER",
                        "PROPOSED",
                        "CONFIRMED"
                );

        when(crewStatusService.getByName("ACTIVE"))
                .thenReturn(activeStatus);

        doThrow(new RuntimeException("Notification failed"))
                .when(notificationService)
                .createRosterStatusNotification(event);

        listener.handle(event);

        verify(driverService)
                .updateCrewStatus(107, activeStatus);

        verify(notificationService)
                .createRosterStatusNotification(event);
    }

    @Test
    void shouldNotUpdateCrewStatusForUnrelatedRosterStatus() {

        RosterShiftStatusChangedEvent event =
                new RosterShiftStatusChangedEvent(
                        9,
                        108,
                        10,
                        "DRIVER",
                        "DRAFT",
                        "PROPOSED"
                );

        listener.handle(event);

        verifyNoInteractions(crewStatusService);
        verifyNoInteractions(driverService);
        verifyNoInteractions(conductorService);

        verify(notificationService)
                .createRosterStatusNotification(event);
    }
}