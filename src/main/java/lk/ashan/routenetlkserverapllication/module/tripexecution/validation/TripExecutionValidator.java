package lk.ashan.routenetlkserverapllication.module.tripexecution.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecutionStatus;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import org.springframework.stereotype.Component;

@Component
public class TripExecutionValidator {

    public void validateStatusTransition(TripExecutionStatus currentStatus, TripExecutionStatus targetStatus) {
        String current =
                normalize(currentStatus.getName());

        String target =
                normalize(targetStatus.getName());

        if (current.equals(target)) {
            return;
        }

        boolean valid = switch (current) {

            case "SCHEDULED" ->
                    target.equals("CHECKED IN")
                            || target.equals("CANCELLED");

            case "CHECKED IN" ->
                    target.equals("DISPATCHED")
                            || target.equals("CANCELLED");

            case "DISPATCHED" ->
                    target.equals("ARRIVED")
                            || target.equals("BREAKDOWN");

            case "ARRIVED" ->
                    target.equals("COMPLETED");

            case "BREAKDOWN",
                 "COMPLETED",
                 "CANCELLED" ->
                    false;

            default ->
                    false;
        };

        if (!valid) {
            throw new InvalidStateTransitionException(
                    "Invalid trip execution status transition from "
                            + current
                            + " to "
                            + target
            );
        }
    }

    public void validateCheckIn(TripExecution tripExecution) {
        Employee driver =
                tripExecution
                        .getDriver()
                        .getEmployee();

        Employee conductor =
                tripExecution
                        .getConductor()
                        .getEmployee();

        if ("On leave".equalsIgnoreCase(
                driver.getEmployeestatus().getName()
        )) {
            throw new BusinessRuleViolationException(
                    "Driver is not Available on Today"
            );
        }

        if ("On leave".equalsIgnoreCase(
                conductor.getEmployeestatus().getName()
        )) {
            throw new BusinessRuleViolationException(
                    "Conductor is not Available on Today"
            );
        }
    }

    private String normalize(String value) {
        return value.trim().toUpperCase();
    }
}