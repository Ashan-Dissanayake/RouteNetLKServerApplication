package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CrewAttendanceContextBuilder {

    public CrewAttendanceContext buildForCheckIn(CrewCheckInRequestDto requestDto){
       return CrewAttendanceContext.builder()
                .attendanceId(requestDto.getAttendanceId())
                .actualEmployeeId(requestDto.getEmployeeId())
                .checkInTime(LocalTime.now())
                .build();

    }

}
