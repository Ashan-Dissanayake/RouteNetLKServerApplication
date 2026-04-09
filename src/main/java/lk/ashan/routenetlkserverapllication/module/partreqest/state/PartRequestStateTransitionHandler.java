package lk.ashan.routenetlkserverapllication.module.partreqest.state;

import lk.ashan.routenetlkserverapllication.module.partreqest.event.PartRequestApprovedEvent;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequest;
import lk.ashan.routenetlkserverapllication.module.partreqest.model.entity.PartRequestStatus;
import lk.ashan.routenetlkserverapllication.module.partreqest.repository.PartRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartRequestStateTransitionHandler {

    private final PartRequestStatusFactory requestStatusFactory;
    private final ApplicationEventPublisher eventPublisher;


    public void transitionTo(PartRequest request, PartRequestStatus newStatus) {

        String currentStatus = request.getPartrequeststatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning request {} from {} to {}",
                request.getId(), currentStatus, targetStatus);

        executeOnExit(request, currentStatus);

        PartRequestState currentState =
                requestStatusFactory.getState(currentStatus);

        currentState.transitionTo(request, newStatus);

        executeOnEnter(request, targetStatus);
    }

    private void executeOnExit(PartRequest request, String statusName) {

        switch (statusName.toUpperCase()) {

            case "PENDING" ->
                    log.debug("Exiting PENDING state for request {}", request.getId());

            case "APPROVED" ->
                    log.debug("Exiting APPROVED state for request {}", request.getId());
        }
    }

    private void executeOnEnter(PartRequest request, String statusName) {
        switch (statusName.toUpperCase()) {
            case "APPROVED" -> {
                log.info("Request {} approved", request.getId());
                // 2. Publish the Event here
                eventPublisher.publishEvent(new PartRequestApprovedEvent(request.getId()));
            }
            case "REJECTED" -> log.info("Request {} rejected", request.getId());
            case "COMPLETED" -> log.info("Request {} completed", request.getId());
        }
    }
}
