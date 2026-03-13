package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import org.springframework.stereotype.Component;

@Component
public class ReplacementEmployeeCheckInStrategy implements CrewCheckInValidationStrategy {

    @Override
    public void validate(CrewAttendanceContext context) {

        Tripcrewattendance attendance = context.getAttendance();
        Employee actual = context.getActualEmployee();

        boolean isPlanned =
                attendance.getPlannedemployee().getId().equals(actual.getId());

        if (isPlanned) {
            throw new BusinessRuleViolationException(
                    "Replacement employee must be different from planned employee"
            );
        }
    }
}
