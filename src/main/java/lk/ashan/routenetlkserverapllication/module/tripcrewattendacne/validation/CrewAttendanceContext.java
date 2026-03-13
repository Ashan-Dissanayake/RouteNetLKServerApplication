package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class CrewAttendanceContext {
    private final Tripcrewattendance attendance;
    private final Employee actualEmployee; // employee performing check-in
    private final LocalTime checkInTime;
}
