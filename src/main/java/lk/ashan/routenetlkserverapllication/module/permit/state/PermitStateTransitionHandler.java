package lk.ashan.routenetlkserverapllication.module.permit.state;

import lk.ashan.routenetlkserverapllication.module.permit.event.PermitTransferredEvent;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermitStateTransitionHandler {

    private final PermitStateFactory permitStateFactory;
    private final ApplicationEventPublisher eventPublisher;

    public void transitionTo(Permite permite, PermiteStatus targetStatus) {
        String currentStatus = permite.getPermitestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning permit {} from {} to {}", permite.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(permite, currentStatus);

        // Validate & transition
        PermitState currentState = permitStateFactory.getState(currentStatus);
        currentState.transitionTo(permite, targetStatus);

        // Entry behavior
        executeOnEnter(permite, target);
    }

    private void executeOnExit(Permite permite, String statusName) {

    }

    private void executeOnEnter(Permite permite, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        if (normalized.equals("TRANSFERRED")) {
            onEnterTransferred(permite);
        }
    }

    private void onEnterTransferred(Permite permite) {
        log.info("Publishing PermitTransferredEvent for permit ID {}", permite.getId());
        eventPublisher.publishEvent(new PermitTransferredEvent(
                permite,
                permite.getVehicle(),
                permite.getBranch()
        ));
    }
}
