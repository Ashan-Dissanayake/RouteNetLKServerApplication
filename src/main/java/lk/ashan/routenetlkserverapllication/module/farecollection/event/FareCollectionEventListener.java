package lk.ashan.routenetlkserverapllication.module.farecollection.event;

import lk.ashan.routenetlkserverapllication.shared.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class FareCollectionEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleFareReconciled(FareReconciledEvent event) {
        notificationService.sendNotificationToBranchAndRole(
                event.branch(),
                "ROLE_MANAGER", // හෝ ROLE_FINANCE වැනි අදාළ රෝල් එක
                "Fare Reconciled Successfully",
                "Fare collection ID " + event.fareCollectionId() + " has been reconciled."
        );
    }
}
