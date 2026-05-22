package lk.ashan.routenetlkserverapllication.module.incident.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentState;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentContextBuilder;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentContext;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentStrategy;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.model.entity.TripExecution;
import lk.ashan.routenetlkserverapllication.module.tripexecution.repository.TripExecutionRepository;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lk.ashan.routenetlkserverapllication.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final TripExecutionRepository tripExecutionRepository;
    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentStatusService incidentStatusService;
    private final IncidentMapper incidentMapper;

    private final List<IncidentStrategy> incidentCreationStrategies;
    private final IncidentStatusFactory incidentStatusFactory;
    private final IncidentStateTransitionHandler incidentStateTransitionHandler;
    private final IncidentContextBuilder incidentContextBuilder;

    @Transactional(readOnly = true)
    public List<IncidentDetailResponseDto> getIncidents() {
        return incidentMapper.toDtoList(incidentRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<IncidentDetailResponseDto> searchIncidents(@NotNull HashMap<String, String> params) {

        List<Incident> incidents = incidentRepository.findAll();

        if (!params.isEmpty()) {

            String incidentTypeId = params.get("ssincidenttype");
            String doReport = params.get("ssdoreport");
            String tripExecutionId = params.get("sstripexecution");

            Stream<Incident> incidentStream = incidents.stream();

            if (incidentTypeId != null)
                incidentStream = incidentStream.filter(t -> t.getIncidenttype().getId() == Integer.parseInt(incidentTypeId));
            if (doReport != null)
                incidentStream = incidentStream.filter(t -> t.getDoreported() == LocalDate.parse(doReport));
            if (tripExecutionId != null)
                incidentStream = incidentStream.filter(t -> t.getTripexecution().getId() == Integer.parseInt(tripExecutionId));

            return incidentMapper.toDtoList(incidentStream.collect(Collectors.toList()));
        }

        return incidentMapper.toDtoList(incidents);
    }

    @Transactional
    public IncidentDetailResponseDto create(IncidentCreateRequestDto dto) {

      TripExecution existTripExecution = tripExecutionRepository.findById(dto.getTripexecution().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip Execution not found"));

        incidentTypeRepository.findById(dto.getIncidenttype().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid incident type"));

        IncidentContext context = incidentContextBuilder.buildForCreate(dto);
        incidentCreationStrategies.stream()
                .filter(s -> s.isApplicable(dto.getIncidenttype().getName()))
                .forEach(s -> s.validate(context));
        Incident incident = incidentMapper.toEntity(dto);

        IncidentStatus incidentStatus = incidentStatusService.getByName(dto.getIncidentstatus().getName());
        IncidentState initialState = incidentStatusFactory.getState(incidentStatus.getName());
        initialState.validateInitial();
        incident.setIncidentstatus(incidentStatus);

        incident.setOdometeratincident(existTripExecution.getEndodometer());
        Incident saved = incidentRepository.save(incident);

        return incidentMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<IncidentSummaryDto> getSummaryIncidents() {
        return incidentMapper.toSummaryDtoList(incidentRepository.findAll());
    }


    @Transactional
    public  IncidentDetailResponseDto inProgress(@NotNull Integer incidentId){
        IncidentStatus inProgressStatus =incidentStatusService.getByName("In Progress");
        Incident existing = getById(incidentId);
        incidentStateTransitionHandler.transitionTo(existing, inProgressStatus);
        return incidentMapper.toDto(existing);
    }

    @Transactional
    public  IncidentDetailResponseDto vehicleRecovery(@NotNull Integer incidentId){
        IncidentStatus recoveryStatus =incidentStatusService.getByName("Vehicle Recovery");
        Incident existing = getById(incidentId);

        String incidentType = existing.getIncidenttype().getName();

        if (incidentType.equals("Accident") ||incidentType.equals("Mechanical Breakdown") ) {
            incidentStateTransitionHandler.transitionTo(existing, recoveryStatus);
        } else {
            throw new BusinessRuleViolationException(
                    "Only MECHANICAL BREAKDOWN or ACCIDENT can be " +
                     "marked for VEHICLE RECOVERY"
            );
        }
        return incidentMapper.toDto(existing);
    }

    @Transactional
    public  IncidentDetailResponseDto pendingAllocation(@NotNull Integer incidentId){
        IncidentStatus pendingStatus =incidentStatusService.getByName("Pending Allocation");
        Incident existing = getById(incidentId);

        String incidentType = existing.getIncidenttype().getName();

        if (incidentType.equals("Mechanical Breakdown") || incidentType.equals("Accident")) {
            incidentStateTransitionHandler.transitionTo(existing, pendingStatus);
        } else {
            throw new BusinessRuleViolationException(
                    "Only MECHANICAL BREAKDOWN or ACCIDENT incidents can be " +
                            "marked for PENDING ALLOCATION"
            );
        }
        return incidentMapper.toDto(existing);
    }

    @Transactional
    public IncidentDetailResponseDto resolved(@NotNull Integer incidentId){
        IncidentStatus resolvedStatus =incidentStatusService.getByName("Resolved");
        Incident existing = getById(incidentId);
        incidentStateTransitionHandler.transitionTo(existing, resolvedStatus);
        return incidentMapper.toDto(existing);
    }

    @Transactional
    public IncidentDetailResponseDto closed(@NotNull Integer incidentId){
        IncidentStatus closedStatus =incidentStatusService.getByName("Closed");
        Incident existing = getById(incidentId);
        incidentStateTransitionHandler.transitionTo(existing, closedStatus);
        return incidentMapper.toDto(existing);
    }

    private Incident getById(@NotNull Integer id){
       return incidentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));
    }
}

