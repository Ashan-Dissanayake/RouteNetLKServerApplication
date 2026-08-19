package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.GrnStatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Handles state transitions for GRN (Goods Received Note) entities.
 * This class is responsible for managing the transition between different states
 * of a GRN and executing actions during state entry and exit.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GrnStateTransitionHandler {

    private final GrnStatusFactory grnStatusFactory;
    private final GrnRepository grnRepository;

    /**
     * Transitions a GRN to a new state.
     *
     * @param grn       The GRN entity to transition.
     * @param newStatus The target state to transition to.
     * @throws IllegalArgumentException if the current or target state is invalid.
     */
    public void transitionTo(Grn grn, GrnStatus newStatus) {

        String currentStatus = grn.getGrnstatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning GRN {} from {} to {}",
                grn.getId(), currentStatus, targetStatus);

        executeOnExit(grn, currentStatus);

        GrnState currentState =
                grnStatusFactory.getState(currentStatus);

        currentState.transitionTo(grn, newStatus);

        executeOnEnter(grn, targetStatus);
    }

    /**
     * Executes actions when exiting a specific state.
     *
     * @param grn        The GRN entity.
     * @param statusName The name of the state being exited.
     */
    private void executeOnExit(Grn grn, String statusName) {

        if (statusName.equalsIgnoreCase("PENDING")) {
            log.debug("Exiting PENDING state for GRN {}", grn.getId());
        }
    }

    /**
     * Executes actions when entering a specific state.
     *
     * @param grn        The GRN entity.
     * @param statusName The name of the state being entered.
     */
    private void executeOnEnter(Grn grn, String statusName) {

        switch (statusName.toUpperCase()) {

            case "COMPLETED" -> log.info("GRN {} completed", grn.getId());

            case "CANCELLED" -> log.info("GRN {} cancelled", grn.getId());
        }
    }
}
