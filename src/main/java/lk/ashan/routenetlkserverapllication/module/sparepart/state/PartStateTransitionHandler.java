package lk.ashan.routenetlkserverapllication.module.sparepart.state;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Part;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.entity.Partstatus;
import lk.ashan.routenetlkserverapllication.module.sparepart.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartStateTransitionHandler {

    private final PartStatusFactory partStatusFactory;

    public void transitionTo(Part part, Partstatus newStatus) {

        String currentStatus = part.getPartstatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning part {} from {} to {}",
                part.getId(), currentStatus, targetStatus);

        executeOnExit(part, currentStatus);

        SparePartState currentState = partStatusFactory.getState(currentStatus);
        currentState.transitionTo(part, newStatus);

        executeOnEnter(part, targetStatus);
    }

    private void executeOnExit(Part part, String statusName) {
        switch (statusName.toUpperCase()) {
            case "AVAILABLE" ->
                    log.debug("Exiting AVAILABLE state for part {}", part.getId());

            case "LOW_STOCK" ->
                    log.debug("Exiting LOW_STOCK state for part {}", part.getId());

            case "OUT_OF_STOCK" ->
                    log.debug("Exiting OUT_OF_STOCK state for part {}", part.getId());
        }
    }

    private void executeOnEnter(Part part, String statusName) {
        switch (statusName.toUpperCase()) {
            case "AVAILABLE" ->
                    log.info("Entering AVAILABLE state for part {}", part.getId());

            case "LOW_STOCK" ->
                    log.info("Entering LOW_STOCK state for part {}", part.getId());

            case "OUT_OF_STOCK" ->
                    log.info("Entering OUT_OF_STOCK state for part {}", part.getId());

            case "DECOMMISSIONED" ->
                    log.info("Entering DECOMMISSIONED state for part {}", part.getId());
        }
    }
}
