package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;

public interface AttendanceState {
    void checkIn(Tripcrewattendance attendance, Employee employee);
    void markAbsent(Tripcrewattendance attendance);
}
