package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import lk.ashan.routenetlkserverapllication.module.roster.model.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RosterStateTransitionHandler {

    private final RosterStatusFactory rosterStatusFactory;
    private final ShiftRosterAssignmentRepository shiftRosterAssignmentRepository;
    private final ShiftRosterAssignmentStatusRepository shiftRosterAssignmentStatusRepository;

    /**
     * Performs state transition with all necessary side effects
     */
    public void transitionTo(Roster roster, Rosterstatus newStatus) {

        String currentStatusName = roster.getRosterstatus().getName();
        String newStatusName = newStatus.getName();

        log.info("Transitioning roster {} from {} to {}",
                roster.getId(), currentStatusName, newStatusName);

        // Execute exit behavior for current state
        executeOnExit(roster, currentStatusName);

        // Validate transition is allowed
        RosterState currentState = rosterStatusFactory.getState(currentStatusName);
        currentState.transitionTo(roster, newStatus);

        // Execute entry behavior for new state
        executeOnEnter(roster, newStatusName);
    }

    /**
     * Executes behavior when EXITING a state
     */
    private void executeOnExit(Roster roster, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        switch (normalized) {
            case "DRAFT"  -> onExitDraft(roster);
            case "LOCKED" -> onExitLocked(roster);
            default       -> { /* No exit behavior for other states */ }
        }
    }

    /**
     * Executes behavior when ENTERING a state
     */
    private void executeOnEnter(Roster roster, String statusName) {
        String normalized = statusName.trim().toUpperCase();

        switch (normalized) {
            case "DRAFT"    -> onEnterDraft(roster);
            case "LOCKED"   -> onEnterLocked(roster);
            case "ARCHIVED" -> onEnterArchived(roster);
            default         -> { /* No entry behavior for other states */ }
        }
    }

    // ==================== EXIT BEHAVIORS ====================

    private void onExitDraft(Roster roster) {
        log.debug("Exiting DRAFT state for roster {}", roster.getId());
        // Could validate roster has minimum assignments before leaving DRAFT
    }

    private void onExitLocked(Roster roster) {
        log.debug("Exiting LOCKED state for roster {}", roster.getId());
        // Could record how long roster was locked
    }

    // ==================== ENTRY BEHAVIORS ====================

    private void onEnterDraft(Roster roster) {
        log.info("Entering DRAFT state for roster {} - editing allowed",
                roster.getId());

        // When unlocking back to DRAFT:
        // Reset all CONFIRMED assignments back to SUGGESTED
        // so they can be re-evaluated
        List<Shiftrosterassignment> confirmedAssignments =
                roster.getShiftrosterassignments().stream()
                        .filter(a -> "CONFIRMED".equalsIgnoreCase(
                                a.getShiftrosterassignmentstatus().getName()))
                        .toList();

        if (!confirmedAssignments.isEmpty()) {
            Shiftrosterassignmentstatus suggestedStatus =
                    shiftRosterAssignmentStatusRepository.findByName("Suggested")
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Assignment status not found: Suggested"
                            ));

            confirmedAssignments.forEach(a ->
                    a.setShiftrosterassignmentstatus(suggestedStatus)
            );

            shiftRosterAssignmentRepository.saveAll(confirmedAssignments);

            log.info("Reset {} confirmed assignment(s) to SUGGESTED for roster {}",
                    confirmedAssignments.size(), roster.getId());
        }
    }

    private void onEnterLocked(Roster roster) {
        log.info("Entering LOCKED state for roster {} - no edits allowed",
                roster.getId());

        // Could send notifications to all assigned employees
        // notificationService.notifyRosterLocked(roster);

        // Could trigger shift confirmation requests for all assignments
        // roster.getAssignments().forEach(a ->
        //     notificationService.requestConfirmation(a)
        // );
    }

    private void onEnterArchived(Roster roster) {
        log.info("Entering ARCHIVED state for roster {} - read only",
                roster.getId());

        // Historical data preserved
        // Could trigger archival report generation
        // reportService.generateRosterReport(roster);

        // Could send summary to management
        // notificationService.notifyManagement(roster);
    }

}
