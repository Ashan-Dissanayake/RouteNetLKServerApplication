package lk.ashan.routenetlkserverapllication.module.grn.state;

import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grn;
import lk.ashan.routenetlkserverapllication.module.grn.model.entity.Grnstatus;
import lk.ashan.routenetlkserverapllication.module.grn.repository.GrnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GrnStateTransitionHandler {

    private final GrnStatusFactory grnStatusFactory;
    private final GrnRepository grnRepository;

    public void transitionTo(Grn grn, Grnstatus newStatus) {

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

    private void executeOnExit(Grn grn, String statusName) {

        if (statusName.equalsIgnoreCase("PENDING")) {
            log.debug("Exiting PENDING state for GRN {}", grn.getId());
        }
    }

    private void executeOnEnter(Grn grn, String statusName) {

        switch (statusName.toUpperCase()) {

            case "COMPLETED" -> log.info("GRN {} completed", grn.getId());

            case "CANCELLED" -> log.info("GRN {} cancelled", grn.getId());
        }
    }
}
