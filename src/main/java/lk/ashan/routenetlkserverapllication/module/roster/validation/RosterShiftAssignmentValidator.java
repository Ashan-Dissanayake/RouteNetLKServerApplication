package lk.ashan.routenetlkserverapllication.module.roster.validation;


import lk.ashan.routenetlkserverapllication.module.roster.model.entity.RosterShiftAssignmentStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class RosterShiftAssignmentValidator {

    /**
     * Validates whether a roster shift assignment can transition
     * from its current status to the requested target status.
     *
     * @param currentStatus current assignment status
     * @param targetStatus requested target status
     * @throws BusinessRuleViolationException if the transition is invalid
     */
    public void validateStatusTransition(
            RosterShiftAssignmentStatus currentStatus,
            RosterShiftAssignmentStatus targetStatus
    ) {

        String current = normalize(currentStatus.getName());
        String target = normalize(targetStatus.getName());

        // No state change required
        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "DRAFT" ->
                    target.equals("PROPOSED")
                            || target.equals("CANCELED");

            case "PROPOSED" ->
                    target.equals("CONFIRMED")
                            || target.equals("CANCELED");

            case "CONFIRMED" ->
                    target.equals("IN-PROGRESS")
                            || target.equals("CANCELED")
                            || target.equals("ABSENT");

            case "IN-PROGRESS" ->
                    target.equals("COMPLETED")
                            || target.equals("CANCELED")
                            || target.equals("ABSENT");

            case "COMPLETED",
                 "CANCELED",
                 "ABSENT" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new BusinessRuleViolationException(
                    "Invalid roster shift assignment status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    private String normalize(String status) {
        return status.trim().toUpperCase();
    }
}