package lk.ashan.routenetlkserverapllication.module.incident.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.entity.IncidentStatus;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentStatusRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentState;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentContextBuilder;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentContext;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentCreationStrategy;
import lk.ashan.routenetlkserverapllication.module.trip.repository.TripRepository;
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
    private final TripRepository tripRepository;
    private final IncidentTypeRepository incidentTypeRepository;
    private final IncidentStatusRepository incidentStatusRepository;
    private final IncidentStatusService incidentStatusService;
    private final IncidentMapper incidentMapper;

    private final List<IncidentCreationStrategy> incidentCreationStrategies;
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
            String tripId = params.get("sstrip");

            Stream<Incident> incidentStream = incidents.stream();

            if (incidentTypeId != null)
                incidentStream = incidentStream.filter(t -> t.getIncidenttype().getId() == Integer.parseInt(incidentTypeId));
            if (doReport != null)
                incidentStream = incidentStream.filter(t -> t.getDoreported() == LocalDate.parse(doReport));
            if (tripId != null)
                incidentStream = incidentStream.filter(t -> t.getTrip().getId() == Integer.parseInt(tripId));

            return incidentMapper.toDtoList(incidentStream.collect(Collectors.toList()));
        }

        return incidentMapper.toDtoList(incidents);
    }

    @Transactional
    public IncidentDetailResponseDto create(IncidentCreateRequestDto dto) {

        tripRepository.findById(dto.getTrip().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        incidentTypeRepository.findById(dto.getIncidenttype().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid incident type"));

        IncidentContext context = incidentContextBuilder.buildForCreate(dto);
        incidentCreationStrategies.forEach(strategy -> strategy.validate(context));

        Incident incident = incidentMapper.toEntity(dto);

        IncidentStatus incidentStatus = incidentStatusService.getByName(dto.getIncidentstatus().getName());
        IncidentState initialState =
                incidentStatusFactory.getState(incidentStatus.getName());
        initialState.validateInitial();

        incident.setIncidentstatus(incidentStatus);
        Incident saved = incidentRepository.save(incident);

        return incidentMapper.toDto(saved);
    }


    @Transactional
    public IncidentDetailResponseDto update(@NotNull IncidentUpdateRequestDto updateRequestDto) {

        Incident existing = incidentRepository.findById(updateRequestDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        IncidentStatus targetStatus = incidentStatusRepository.findByName(updateRequestDto.getIncidentstatus().getName())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid incident status"));

        Incident mappedIncident = incidentMapper.updateEntityFromDto(updateRequestDto,existing);

        incidentStateTransitionHandler.transitionTo(mappedIncident, targetStatus);

        return incidentMapper.toDto(mappedIncident);
    }
}

