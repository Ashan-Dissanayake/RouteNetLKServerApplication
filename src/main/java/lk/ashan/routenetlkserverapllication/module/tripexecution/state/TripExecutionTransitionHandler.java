package lk.ashan.routenetlkserverapllication.module.tripexecution.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TripExecutionTransitionHandler {

    private final TripExecutionStatusFactory tripExecutionStatusFactory;

    @Transactional
    public void transitionTo(TripExecution tripExecution, TripExecutionStatus targetStatusEntity) {
        String currentStatusName = tripExecution.getTripexecutionstatus().getName();
        String targetStatusName = targetStatusEntity.getName();

        log.info("Attempting transition for Trip {} from [{}] to [{}]",
                tripExecution, currentStatusName, targetStatusName);

        executeOnExit(tripExecution, currentStatusName);

        TripExecutionState currentStateLogic = tripExecutionStatusFactory.getState(currentStatusName);
        currentStateLogic.transitionTo(tripExecution, targetStatusEntity);

        executeOnEnter(tripExecution, targetStatusName);

        log.info("Successfully transitioned Trip {} to {}", tripExecution.getId(), targetStatusName);
    }

    private void executeOnExit(TripExecution trip, String statusName) {
        if ("SCHEDULED".equalsIgnoreCase(statusName)) {
            log.debug("Trip {} is no longer in pure Scheduled state. Preparing for operation.", trip.getId());
        }
    }

    private void executeOnEnter(TripExecution trip, String statusName) {
        switch (statusName.toUpperCase()) {
            case "CANCELLED" -> log.warn("Trip {} has been marked as CANCELLED.", trip.getId());
            case "BREAKDOWN" -> log.error("BREAKDOWN reported for Trip {}. Emergency protocols triggered.", trip.getId());
            case "COMPLETED" -> log.info("Trip {} completed. Ready for revenue processing.", trip.getId());
        }
    }

}
