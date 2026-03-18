package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation;

import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceUpdateRequestDto;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class CrewAttendanceContextBuilder {

    public CrewAttendanceContext buildForCheckIn(TripCrewAttendanceUpdateRequestDto requestDto){
       return CrewAttendanceContext.builder()
                .attendanceId(requestDto.getId())
                .actualEmployeeId(requestDto.getActualemployee().getId())
                .checkInTime(LocalTime.now())
                .build();
    }

}
