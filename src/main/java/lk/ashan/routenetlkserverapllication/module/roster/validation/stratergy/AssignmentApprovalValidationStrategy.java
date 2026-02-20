package lk.ashan.routenetlkserverapllication.module.roster.validation.stratergy;


import lk.ashan.routenetlkserverapllication.module.roster.model.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates assignment can only be approved when roster is DRAFT
 */
@Component
@RequiredArgsConstructor
public class AssignmentApprovalValidationStrategy {

    private final ShiftRosterAssignmentRepository assignmentRepository;

    public void validate(Integer assignmentId) {

        Shiftrosterassignment assignment = assignmentRepository
                .findById(assignmentId)
                .orElseThrow(() -> new BusinessRuleViolationException(
                        "Assignment not found with ID: " + assignmentId
                ));

        // 1. Must be SUGGESTED status
        if (!"SUGGESTED".equalsIgnoreCase(
                assignment.getShiftrosterassignmentstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only approve SUGGESTED assignments. " +
                            "Current status: " + assignment.getShiftrosterassignmentstatus().getName()
            );
        }

        // 2. Roster must be DRAFT
        if (!"DRAFT".equalsIgnoreCase(
                assignment.getRoster().getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only approve assignments for DRAFT rosters. " +
                            "Current roster status: " + assignment.getRoster().getRosterstatus().getName()
            );
        }
    }
}
