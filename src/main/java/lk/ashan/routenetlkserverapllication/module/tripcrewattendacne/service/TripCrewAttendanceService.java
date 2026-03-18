package lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.entity.Branch;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.mapper.TripCrewAttendanceMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.mapper.TripCrewAttendanceStatusMapper;
import lk.ashan.routenetlkserverapllication.module.tripcrewattendacne.model.dto.TripCrewAttendanceDetailsResponseDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TripCrewAttendanceService {

    private final TripCrewAttendanceRepository tripCrewAttendanceRepository;
    private final CrewAttendanceStatusRepository crewAttendanceRepository;
    private final TripCrewAttendanceMapper tripCrewAttendanceMapper;

    private final List<CrewCheckInValidationStrategy> validationStrategies;
    private final CrewAttendanceContextBuilder attendanceContextBuilder;
    private final TripCrewAttendanceStateTransitionHandler stateTransitionHandler;


    @Transactional(readOnly = true)
    public List<TripCrewAttendanceDetailsResponseDto> getCrewAttendance(){
        return tripCrewAttendanceMapper.toDtoList(tripCrewAttendanceRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<TripCrewAttendanceDetailsResponseDto> searchTripCrewAttendance(@NotNull HashMap<String, String> params) {

            String tripId = params.get("sstrip");
            String attendanceStatusId= params.get("ssattendacncestatus");

            Stream<TripCrewAttendance> tripCrewAttendanceStream = tripCrewAttendanceRepository.findAll().stream();

            if(tripId!=null)tripCrewAttendanceStream = tripCrewAttendanceStream.filter(t->t.getTrip().getId() == Integer.parseInt(tripId));
            if(attendanceStatusId!=null)tripCrewAttendanceStream = tripCrewAttendanceStream.filter(t->t.getCrewattendancestatus().getId() == Integer.parseInt(attendanceStatusId));

            return tripCrewAttendanceMapper.toDtoList( tripCrewAttendanceStream.collect(Collectors.toList()));
    }



}
