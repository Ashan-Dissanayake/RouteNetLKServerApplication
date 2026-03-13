package lk.ashan.routenetlkserverapllication.module.roster.state.roster;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Roster;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Rosterstatus;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignmentstatus;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentStatusRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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

        // Validate roster has at least one assignment before leaving DRAFT
        List<Shiftrosterassignment> assignments =
                shiftRosterAssignmentRepository.findByRoster_Id(roster.getId());

        if (assignments.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Cannot lock roster without any shift assignments"
            );
        }

        log.info("Roster {} has {} assignments, ready to lock",
                roster.getId(), assignments.size());
    }

    private void onExitLocked(Roster roster) {
        log.debug("Exiting LOCKED state for roster {}", roster.getId());
        // Could record how long roster was locked
    }

    // ==================== ENTRY BEHAVIORS ====================

    private void onEnterDraft(Roster roster) {
        log.info("Entering DRAFT state for roster {} - editing allowed", roster.getId());

        //When unlocking back to DRAFT: Reset CONFIRMED assignments to SUGGESTED
        Shiftrosterassignmentstatus suggestedStatus = shiftRosterAssignmentStatusRepository
                .findByName("Suggested")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Assignment status not found: Suggested"
                ));

        List<Shiftrosterassignment> confirmedAssignments =
                shiftRosterAssignmentRepository.findByRoster_Id(roster.getId())
                        .stream()
                        .filter(a -> "CONFIRMED".equalsIgnoreCase(
                                a.getShiftrosterassignmentstatus().getName()))
                        .collect(Collectors.toList());

        if (!confirmedAssignments.isEmpty()) {
            confirmedAssignments.forEach(a ->
                    a.setShiftrosterassignmentstatus(suggestedStatus)
            );

            shiftRosterAssignmentRepository.saveAll(confirmedAssignments);

            log.info("Reset {} confirmed assignment(s) to SUGGESTED for roster {}",
                    confirmedAssignments.size(), roster.getId());
        }
    }

    private void onEnterLocked(Roster roster) {
        log.info("Entering LOCKED state for roster {} - no edits allowed", roster.getId());

        // When locking: All SUGGESTED assignments remain SUGGESTED
        // Employees will confirm/reject them
        // No automatic status change here

        // Could send notifications to employees here
        List<Shiftrosterassignment> suggestedAssignments =
                shiftRosterAssignmentRepository
                        .findByRoster_IdAndShiftrosterassignmentstatus_Name(
                                roster.getId(),
                                "Suggested"
                        );

        log.info("Roster locked with {} suggested assignments awaiting confirmation",
                suggestedAssignments.size());

        // TODO: Send notifications to employees
        // notificationService.notifyEmployeesOfAssignments(suggestedAssignments);
    }

    private void onEnterArchived(Roster roster) {
        log.info("Entering ARCHIVED state for roster {} - read only", roster.getId());

        //Validate all assignments are CONFIRMED before archiving
        List<Shiftrosterassignment> unconfirmedAssignments =
                shiftRosterAssignmentRepository.findByRoster_Id(roster.getId())
                        .stream()
                        .filter(a -> !"CONFIRMED".equalsIgnoreCase(
                                a.getShiftrosterassignmentstatus().getName()))
                        .toList();

        if (!unconfirmedAssignments.isEmpty()) {
            throw new BusinessRuleViolationException(
                    "Cannot archive roster with " + unconfirmedAssignments.size() +
                            " unconfirmed assignment(s). All assignments must be confirmed first."
            );
        }

        log.info("All {} assignments confirmed, roster ready for archival",
                shiftRosterAssignmentRepository.findByRoster_Id(roster.getId()).size());

        // Could trigger archival report generation here
        // reportService.generateRosterReport(roster);
    }
}
