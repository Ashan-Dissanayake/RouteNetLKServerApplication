package lk.ashan.routenetlkserverapllication.module.roster.validation;

import lk.ashan.routenetlkserverapllication.module.roster.model.entity.Shiftrosterassignment;
import lk.ashan.routenetlkserverapllication.module.roster.repository.ShiftRosterAssignmentRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Validates assignment can only be rejected when roster is DRAFT
 */
@Component
@RequiredArgsConstructor
public class AssignmentRejectionValidationStrategy {

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
                    "Can only reject SUGGESTED assignments. " +
                            "Current status: " + assignment.getShiftrosterassignmentstatus().getName()
            );
        }

        // 2. Roster must be DRAFT
        if (!"DRAFT".equalsIgnoreCase(
                assignment.getRoster().getRosterstatus().getName())) {
            throw new BusinessRuleViolationException(
                    "Can only reject assignments for DRAFT rosters. " +
                            "Current roster status: " + assignment.getRoster().getRosterstatus().getName()
            );
        }
    }
}
