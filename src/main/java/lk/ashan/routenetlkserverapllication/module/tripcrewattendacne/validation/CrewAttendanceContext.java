package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class CrewAttendanceContext {
    private final Integer attendanceId;
    private final Integer actualEmployeeId; // employee performing check-in
    private final LocalTime checkInTime;
}
