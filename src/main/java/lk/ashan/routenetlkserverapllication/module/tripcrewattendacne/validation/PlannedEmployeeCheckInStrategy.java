package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class PlannedEmployeeCheckInStrategy implements CrewCheckInValidationStrategy {

    @Override
    public void validate(CrewAttendanceContext context) {

        Tripcrewattendance attendance = context.getAttendance();
        Employee actual = context.getActualEmployee();

        boolean isPlanned =
                attendance.getPlannedemployee().getId().equals(actual.getId());

        if (!isPlanned) {
            throw new BusinessRuleViolationException(
                    "Only planned employee can use planned check-in"
            );
        }
    }
}
