package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state;

import lk.ashan.routenetlkserverapllication.module.employee.model.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.CrewAttendanceStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class PendingAttendanceState implements AttendanceState {

    private final CrewAttendanceStatusRepository statusRepository;

    @Override
    public void checkIn(Tripcrewattendance attendance, Employee employee) {

        boolean isPlanned =
                attendance.getPlannedemployee().getId().equals(employee.getId());

        attendance.setActualemployee(employee);
        attendance.setTocheckin(LocalTime.now());

        String nextStatus = isPlanned ? "PRESENT" : "REPLACED";

        attendance.setCrewattendancestatus(
                statusRepository.findByName(nextStatus).orElseThrow()
        );
    }

    @Override
    public void markAbsent(Tripcrewattendance attendance) {

        attendance.setCrewattendancestatus(
                statusRepository.findByName("ABSENT").orElseThrow()
        );
    }
}
