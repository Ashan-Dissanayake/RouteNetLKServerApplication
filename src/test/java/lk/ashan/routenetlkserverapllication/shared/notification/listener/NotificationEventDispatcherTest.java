package lk.ashan.routenetlkserverapllication.shared.notification.listener;

import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.farecollection.event.FareReconciledEvent;
import lk.ashan.routenetlkserverapllication.module.grn.event.PartReceivedEvent;
import lk.ashan.routenetlkserverapllication.module.partreqest.event.PartRequestApprovedEvent;
import lk.ashan.routenetlkserverapllication.module.permit.event.PermitTransferredEvent;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.entity.Vehicle;
import lk.ashan.routenetlkserverapllication.shared.notification.model.AppRoles;
import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationEventDispatcherTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationEventDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        dispatcher.init();
    }

    @Test
    void shouldSendNotificationWhenPermitIsTransferred() {

        Branch branch = mock(Branch.class);
        Permite permit = mock(Permite.class);
        Vehicle vehicle = mock(Vehicle.class);

        when(permit.getId()).thenReturn(100);
        when(permit.getNumber()).thenReturn("RP-001");
        when(vehicle.getNumber()).thenReturn("BUS-123");

        PermitTransferredEvent event =
                new PermitTransferredEvent(
                        permit,
                        vehicle,
                        branch
                                );

        dispatcher.handleEvent(event);

        verify(notificationService).sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Route Permit Transferred",
                "Route permit #RP-001 (Bus: BUS-123) has been transferred."
        );
    }

    @Test
    void shouldSendNotificationWhenFareIsReconciled() {

        Branch branch = mock(Branch.class);

        FareReconciledEvent event =
                new FareReconciledEvent(
                        200,
                        branch
                );

        dispatcher.handleEvent(event);

        verify(notificationService).sendNotificationToBranchAndRole(
                branch,
                AppRoles.DEPOT_MANAGER,
                "Fare Reconciled Successfully",
                "Fare collection ID 200 has been reconciled."
        );
    }

    @Test
    void shouldSendNotificationWhenPartRequestIsApproved() {

        Branch branch = mock(Branch.class);

        PartRequestApprovedEvent event =
                new PartRequestApprovedEvent(
                        branch,
            300
                );

        dispatcher.handleEvent(event);

        verify(notificationService).sendNotificationToBranchAndRole(
                branch,
                AppRoles.INVENTORY_OFFICER,
                "Part Request Approved",
                "Part request ID 300 has been approved."
        );
    }

    @Test
    void shouldSendNotificationWhenPartIsReceived() {

        Branch branch = mock(Branch.class);

        PartReceivedEvent event =
                new PartReceivedEvent(
                        branch,
                        400,
                        BigDecimal.TEN
                );

        dispatcher.handleEvent(event);

        verify(notificationService).sendNotificationToBranchAndRole(
                branch,
                AppRoles.MAINTENANCE_OFFICER,
                "Part Received",
                "Part ID 400 has been received."
        );
    }

    @Test
    void shouldNotSendNotificationForUnknownEvent() {

        Object unknownEvent = new Object();

        dispatcher.handleEvent(unknownEvent);

        verifyNoInteractions(notificationService);
    }
}