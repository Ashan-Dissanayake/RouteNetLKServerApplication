package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.employee.model.entity.Employee;
import lk.ashan.routenetlkserverapllication.module.employee.repository.EmployeeRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.CrewCheckInRequestDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.Tripcrewattendance;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.TripCrewAttendanceRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state.AttendanceState;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state.AttendanceStateFactory;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.AbsentValidationStrategy;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.CrewAttendanceContext;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.CrewCheckInValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCrewAttendanceService {

    private final List<CrewCheckInValidationStrategy> validationStrategies;
    private final AttendanceStateFactory stateFactory;
    private final AbsentValidationStrategy absentValidationStrategy;
    private final TripCrewAttendanceRepository tripCrewAttendanceRepository;
    private final EmployeeRepository employeeRepository;


    public void checkIn(CrewCheckInRequestDto requestDto) {

        Tripcrewattendance attendance = tripCrewAttendanceRepository.findById(requestDto.getAttendanceId())
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        Employee actual = employeeRepository.findById(requestDto.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        CrewAttendanceContext context = CrewAttendanceContext.builder()
                .attendance(attendance)
                .actualEmployee(actual)
                .checkInTime(LocalTime.now())
                .build();

        validationStrategies.forEach(strategy -> strategy.validate(context));

        AttendanceState state = stateFactory.getState(
                attendance.getCrewattendancestatus().getName()
        );

        state.checkIn(attendance, actual);
    }

    @Transactional
    public void markAbsent(Integer attendanceId) {

        Tripcrewattendance attendance = tripCrewAttendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        absentValidationStrategy.validate(attendance);

        AttendanceState state = stateFactory.getState(
                attendance.getCrewattendancestatus().getName()
        );

        state.markAbsent(attendance);
    }
}
