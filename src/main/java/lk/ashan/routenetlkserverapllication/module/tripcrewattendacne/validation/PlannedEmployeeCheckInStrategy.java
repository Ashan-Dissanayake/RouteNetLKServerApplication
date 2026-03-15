package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.TripCrewAttendanceRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlannedEmployeeCheckInStrategy implements CrewCheckInValidationStrategy {

    private final TripCrewAttendanceRepository tripCrewAttendanceRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void validate(CrewAttendanceContext context) {
        TripCrewAttendance attendance =tripCrewAttendanceRepository.findById(context.getAttendanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        Employee actual = employeeRepository.findById(context.getActualEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        boolean isPlanned =
                attendance.getPlannedemployee().getId().equals(actual.getId());

        if (!isPlanned) {
            throw new BusinessRuleViolationException(
                    "Only planned employee can use planned check-in"
            );
        }
    }
}
