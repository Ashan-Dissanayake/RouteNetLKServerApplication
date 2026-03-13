package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;

public interface AttendanceState {
    void checkIn(Tripcrewattendance attendance, Employee employee);
    void markAbsent(Tripcrewattendance attendance);
}
