package lk.ashan.routenetlkserverapllication.module.permit.state;


import lk.ashan.routenetlkserverapllication.module.permit.model.entity.Permite;
import lk.ashan.routenetlkserverapllication.module.permit.model.entity.PermiteStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PermitStateTransitionHandler {

    private final PermitStateFactory permitStateFactory;

    public void transitionTo(Permite permit, PermiteStatus targetStatus) {
        String currentStatus = permit.getPermitestatus().getName();
        String target = targetStatus.getName();

        log.info("Transitioning permit {} from {} to {}", permit.getId(), currentStatus, target);

        // Exit behavior
        executeOnExit(permit, currentStatus);

        // Validate & transition
        PermitState currentState = permitStateFactory.getState(currentStatus);
        currentState.transitionTo(permit, targetStatus);

        // Entry behavior
        executeOnEnter(permit, target);
    }

    private void executeOnExit(Permite permit, String statusName) {
        log.debug("Exiting {} state for permit {}", statusName, permit.getId());
    }

    private void executeOnEnter(Permite permit, String statusName) {
        log.info("Entering {} state for permit {}", statusName, permit.getId());
    }
}
