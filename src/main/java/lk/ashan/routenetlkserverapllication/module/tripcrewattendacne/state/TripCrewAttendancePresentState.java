package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;
import org.springframework.stereotype.Component;

@Component
public class TripCrewAttendancePresentState implements AttendanceState {

    @Override
    public void checkIn(Tripcrewattendance attendance, Employee employee) {
        throw new IllegalStateException("Already checked in");
    }

    @Override
    public void markAbsent(Tripcrewattendance attendance) {
        throw new IllegalStateException("Cannot mark absent after check-in");
    }
}
