package lk.ashan.routenetlkserverapllication.module.roster.state.rosterassigment;

import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShiftRosterAssignmentStateTransitionHandler {

    private final ShiftRosterAssignmentStatusFactory shiftRosterAssignmentStatusFactory;

    /**
     * Performs state transition with all necessary side effects
     */
    public void transitionTo(Shiftrosterassignment assignment,
                             Shiftrosterassignmentstatus newStatus) {

        String currentStatusName = assignment.getShiftrosterassignmentstatus().getName();
        String newStatusName = newStatus.getName();

        log.info("Transitioning assignment {} from {} to {}",
                assignment.getId(), currentStatusName, newStatusName);

        // Execute exit behavior for current state
        executeOnExit(assignment, currentStatusName);

        // Validate transition is allowed
        ShiftRosterAssignmentState currentState =
                shiftRosterAssignmentStatusFactory.getState(currentStatusName);
        currentState.transitionTo(assignment, newStatus);

        // Execute entry behavior for new state
        executeOnEnter(assignment, newStatusName);
    }

    /**
     * Executes behavior when EXITING a state
     */
    private void executeOnExit(Shiftrosterassignment assignment, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        switch (normalized) {
            case "SUGGESTED" -> onExitSuggested(assignment);
            case "CONFIRMED" -> onExitConfirmed(assignment);
            case "REJECTED"  -> onExitRejected(assignment);
            default          -> { }
        }
    }

    /**
     * Executes behavior when ENTERING a state
     */
    private void executeOnEnter(Shiftrosterassignment assignment, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        switch (normalized) {
            case "SUGGESTED" -> onEnterSuggested(assignment);
            case "CONFIRMED" -> onEnterConfirmed(assignment);
            case "REJECTED"  -> onEnterRejected(assignment);
            default          -> { }
        }
    }

    // ==================== EXIT BEHAVIORS ====================

    private void onExitSuggested(Shiftrosterassignment assignment) {
        log.debug("Exiting SUGGESTED state for assignment {}",
                assignment.getId());
        // Could record how long suggestion was pending
    }

    private void onExitConfirmed(Shiftrosterassignment assignment) {
        log.debug("Exiting CONFIRMED state for assignment {}",
                assignment.getId());
        // Triggered when roster unlocked back to DRAFT
        // Could notify employee that confirmation was reset
    }

    private void onExitRejected(Shiftrosterassignment assignment) {
        log.debug("Exiting REJECTED state for assignment {}",
                assignment.getId());
        // Being re-suggested after rejection
    }

    // ==================== ENTRY BEHAVIORS ====================

    private void onEnterSuggested(Shiftrosterassignment assignment) {
        log.info("Assignment {} SUGGESTED - notifying employee {}",
                assignment.getId(),
                assignment.getEmployee().getId());

        // Send notification to employee to confirm/reject
        // notificationService.notifySuggestion(assignment);
    }

    private void onEnterConfirmed(Shiftrosterassignment assignment) {
        log.info("Assignment {} CONFIRMED by employee {}",
                assignment.getId(),
                assignment.getEmployee().getId());

        // Update roster completion tracking
        // Could check if all assignments confirmed → auto-lock roster
        // notificationService.notifyConfirmation(assignment);
    }

    private void onEnterRejected(Shiftrosterassignment assignment) {
        log.info("Assignment {} REJECTED by employee {}",
                assignment.getId(),
                assignment.getEmployee().getId());

        // Trigger re-assignment suggestion
        // notificationService.notifyRejection(assignment);
        // solverService.suggestAlternative(assignment);
    }
}
