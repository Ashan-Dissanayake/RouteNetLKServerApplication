package lk.ashan.routenetlkserverapllication.module.roster.event;

import lk.ashan.routenetlkserverapllication.module.crew.model.entity.CrewStatus;
import lk.ashan.routenetlkserverapllication.module.crew.service.ConductorService;
import lk.ashan.routenetlkserverapllication.module.crew.service.CrewStatusService;
import lk.ashan.routenetlkserverapllication.module.crew.service.DriverService;
import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class RosterShiftStatusChangedEventListener {

    private final DriverService driverService;
    private final ConductorService conductorService;
    private final CrewStatusService crewStatusService;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(RosterShiftStatusChangedEvent event) {

        try {

            if (isCrewActivationStatus(event.newStatus())) {

                CrewStatus crewStatus =
                        crewStatusService.getByName("ACTIVE");

                updateCrewStatus(event, crewStatus);
            }

            if (isCrewDeactivationStatus(event.newStatus())) {

                CrewStatus crewStatus =
                        crewStatusService.getByName("INACTIVE");

                updateCrewStatus(event, crewStatus);
            }

        } catch (Exception ex) {

            log.error(
                    "Failed to update crew status for assignment {}",
                    event.assignmentId(),
                    ex
            );
        }

        try {

            notificationService.createRosterStatusNotification(event);

        } catch (Exception ex) {

            log.error(
                    "Failed to create notification for assignment {}",
                    event.assignmentId(),
                    ex
            );
        }
    }

    private void updateCrewStatus(
            RosterShiftStatusChangedEvent event,
            CrewStatus crewStatus
    ) {

        switch (event.crewType().toUpperCase()) {

            case "DRIVER" ->
                    driverService.updateCrewStatus(
                            event.employeeId(),
                            crewStatus
                    );

            case "CONDUCTOR" ->
                    conductorService.updateCrewStatus(
                            event.employeeId(),
                            crewStatus
                    );

            default ->
                    throw new IllegalArgumentException(
                            "Unknown crew type: " + event.crewType()
                    );
        }
    }

    private boolean isCrewActivationStatus(String status) {
        return "CONFIRMED".equalsIgnoreCase(status)
                || "IN-PROGRESS".equalsIgnoreCase(status);
    }

    private boolean isCrewDeactivationStatus(String status) {
        return "COMPLETED".equalsIgnoreCase(status)
                || "CANCELED".equalsIgnoreCase(status)
                || "ABSENT".equalsIgnoreCase(status);
    }
}