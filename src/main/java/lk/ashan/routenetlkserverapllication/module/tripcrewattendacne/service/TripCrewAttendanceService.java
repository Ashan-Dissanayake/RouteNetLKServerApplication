package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.service;

import jakarta.transaction.Transactional;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.CrewAttendanceStatus;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.entity.TripCrewAttendance;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.CrewAttendanceStatusRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.repository.TripCrewAttendanceRepository;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.state.TripCrewAttendanceStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.CrewAttendanceContext;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.CrewAttendanceContextBuilder;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.validation.CrewCheckInValidationStrategy;
import lk.ashan.routenetlkserverapllication.shared.exception.InvalidStateTransitionException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripCrewAttendanceService {

    private final TripCrewAttendanceRepository tripCrewAttendanceRepository;
    private final CrewAttendanceStatusRepository crewAttendanceRepository;

    private final List<CrewCheckInValidationStrategy> validationStrategies;
    private final CrewAttendanceContextBuilder attendanceContextBuilder;
    private final TripCrewAttendanceStateTransitionHandler stateTransitionHandler;

    @Transactional
    public void checkIn(CrewCheckInRequestDto requestDto) {
        CrewAttendanceContext context = attendanceContextBuilder.buildForCheckIn(requestDto);
        validationStrategies.forEach(strategy -> strategy.validate(context));

        TripCrewAttendance tripcrewattendance = tripCrewAttendanceRepository.findById(requestDto.getAttendanceId())
                .orElseThrow(()->new ResourceNotFoundException("Attendance not found"));

        CrewAttendanceStatus checkInStatus = crewAttendanceRepository.findByName("Checkin")
                .orElseThrow(()->new ResourceNotFoundException("Status not found"));

        stateTransitionHandler.transitionTo(tripcrewattendance,checkInStatus);
    }

    @Transactional
    public void markAbsent(Integer attendanceId) {
        TripCrewAttendance attendance = tripCrewAttendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Attendance not found"));

        String status = attendance.getCrewattendancestatus().getName();

        if (!"PENDING".equalsIgnoreCase(status)) {
            throw new InvalidStateTransitionException(
                    "Only PENDING attendance can be marked ABSENT"
            );
        }

        CrewAttendanceStatus absentStatus = crewAttendanceRepository.findByName("Absent")
                .orElseThrow(()->new ResourceNotFoundException("Status not found"));

        stateTransitionHandler.transitionTo(attendance,absentStatus);
    }
}
