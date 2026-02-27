package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import org.springframework.stereotype.Component;

@Component
public class PresentAttendanceState implements AttendanceState {

    @Override
    public void checkIn(Tripcrewattendance attendance, Employee employee) {
        throw new IllegalStateException("Already checked in");
    }

    @Override
    public void markAbsent(Tripcrewattendance attendance) {
        throw new IllegalStateException("Cannot mark absent after check-in");
    }
}
