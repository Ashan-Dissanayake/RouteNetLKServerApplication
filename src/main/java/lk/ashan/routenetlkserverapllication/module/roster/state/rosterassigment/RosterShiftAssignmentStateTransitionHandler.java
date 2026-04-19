package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.event.RosterConfirmedEvent;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RosterShiftAssignmentStateTransitionHandler {
    private final RosterShiftAssignmentStatusFactory statusFactory;

    public void transitionTo(RosterShiftAssignment assignment, RosterShiftAssignmentStatus newStatus) {
        String currentStatus = assignment.getRostershiftassignmentstatus().getName();
        String targetStatus = newStatus.getName();

        log.info("Transitioning assignment {} from {} to {}",
                assignment.getId(), currentStatus, targetStatus);

        executeOnExit(assignment, currentStatus);

        RosterShiftAssignmentState currentState = statusFactory.getState(currentStatus);
        currentState.transitionTo(assignment, newStatus);

        executeOnEnter(assignment, targetStatus);
    }

    private void executeOnExit(RosterShiftAssignment assignment, String statusName) {
        switch (statusName.toUpperCase()) {
            case "PROPOSED" ->
                    log.debug("Manager/System moving away from AI proposal for ID: {}", assignment.getId());

            case "IN-PROGRESS" -> {
                log.info("Shift ID {} is finishing. Finalizing trip data.", assignment.getId());
                // Logical Side Effect: You might want to set the actual completion timestamp here
                // assignment.setActualEndTime(LocalDateTime.now());
            }
        }
    }

    private void executeOnEnter(RosterShiftAssignment assignment, String statusName) {
        switch (statusName.toUpperCase()) {
            case "PROPOSED" ->
                    log.info("AI Solver has successfully assigned Employee {} to Shift {}",
                            assignment.getEmployee().getFullname(), assignment.getId());

            case "CONFIRMED" -> {
                log.info("Shift {} is officially confirmed.", assignment.getId());
                // CRITICAL SIDE EFFECT: Notify the employee via SMS/Push
                // notificationService.sendShiftConfirmation(assignment.getEmployee(), assignment.getRostershift());
            }

            case "IN-PROGRESS" -> {
                log.info("Crew has clocked in for Shift {}. Bus is now on route.", assignment.getId());
                // Side Effect: Mark the assigned bus as 'UNAVAILABLE' in the fleet table
            }

            case "COMPLETED" -> {
                log.info("Shift {} successfully completed.", assignment.getId());
                // Side Effect: Trigger payroll calculation or update total hours worked for the employee
            }

            case "CANCELLED" -> {
                log.warn("ALERT: Shift {} has been CANCELLED.", assignment.getId());
                // Side Effect: If it was already confirmed, notify the driver they don't need to come
                // notificationService.sendCancellationNotice(assignment.getEmployee());
            }
        }
    }
}
