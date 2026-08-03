package lk.ashan.routenetlkserverapllication.shared.notification.listener;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import lk.ashan.routenetlkserverapllication.module.grn.event.PartReceivedEvent;
import lk.ashan.routenetlkserverapllication.module.partreqest.event.PartRequestApprovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import lk.ashan.routenetlkserverapllication.module.permit.event.PermitTransferredEvent;
import lk.ashan.routenetlkserverapllication.module.farecollection.event.FareReconciledEvent;
import lk.ashan.routenetlkserverapllication.shared.notification.model.AppRoles;
import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;

/**
 * Central dispatcher that listens to all domain events that should trigger user notifications.
 * It delegates to {@link NotificationService} and keeps the event‑to‑notification mapping in one place.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventDispatcher {

    private final NotificationService notificationService;
    private final Map<Class<?>, Consumer<Object>> handlers = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        handlers.put(PermitTransferredEvent.class, e -> handlePermitTransferred((PermitTransferredEvent) e));
        handlers.put(FareReconciledEvent.class, e -> handleFareReconciled((FareReconciledEvent) e));
        handlers.put(PartRequestApprovedEvent.class, e -> handlePartRequestApprove((PartRequestApprovedEvent) e));
        handlers.put(PartReceivedEvent.class, e -> handlePartReceive((PartReceivedEvent) e));
        log.info("NotificationEventDispatcher initialized with {} handlers", handlers.size());
    }

    @EventListener
    @Async
    public void handleEvent(Object event) {
        Consumer<Object> handler = handlers.get(event.getClass());
        if (handler != null) {
            handler.accept(event);
        } else {
            log.debug("No notification handler registered for event type {}", event.getClass().getSimpleName());
        }
    }

    private void handlePermitTransferred(PermitTransferredEvent event) {
        log.info("NotificationEventDispatcher: Sending notification for transferred permit ID: {}", event.permit().getId());
        if (event.branch() != null) {
            String vehicleInfo = event.vehicle() != null ? " (Bus: " + event.vehicle().getNumber() + ")" : "";
            notificationService.sendNotificationToBranchAndRole(
                    event.branch(),
                    AppRoles.DEPOT_MANAGER,
                    "Route Permit Transferred",
                    "Route permit #" + event.permit().getNumber() + vehicleInfo + " has been transferred."
            );
        }
    }

    private void handleFareReconciled(FareReconciledEvent event) {
        notificationService.sendNotificationToBranchAndRole(
                event.branch(),
                AppRoles.DEPOT_MANAGER,
                "Fare Reconciled Successfully",
                "Fare collection ID " + event.fareCollectionId() + " has been reconciled."
        );
    }

    private  void handlePartRequestApprove(PartRequestApprovedEvent event){
        notificationService.sendNotificationToBranchAndRole(
                event.branch(),
                AppRoles.INVENTORY_OFFICER,
                "Part Request Approved",
                "Part request ID " + event.partRequestId() + " has been approved."
        );
    }

    private  void handlePartReceive(PartReceivedEvent event){
        notificationService.sendNotificationToBranchAndRole(
                event.branch(),
                AppRoles.MAINTENANCE_OFFICER,
                "Part Received",
                "Part ID " + event.partId() + " has been received."
        );
    }
}
