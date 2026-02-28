package lk.ashan.routenetlkserverapllication.module.incident.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidentstatus;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incidenttype;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentStatusRepository;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentTypeRepository;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentState;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStateTransitionHandler;
import lk.ashan.routenetlkserverapllication.module.incident.state.IncidentStatusFactory;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentCreationContext;
import lk.ashan.routenetlkserverapllication.module.incident.validation.IncidentCreationStrategy;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
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
    private final IncidentMapper incidentMapper;
    private final List<IncidentCreationStrategy> incidentCreationStrategies;
    private final IncidentStatusFactory incidentStatusFactory;
    private final IncidentStateTransitionHandler incidentStateTransitionHandler;


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

        Trip trip = tripRepository.findById(dto.getTrip().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));

        Incidenttype incidentType = incidentTypeRepository.findById(dto.getIncidenttype().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid incident type"));

        Incident incident = incidentMapper.toEntity(dto);

        IncidentCreationContext context = new IncidentCreationContext(
                trip,
                incidentType,
                dto.getToreported(),
                dto.getRemarks()
        );

        incidentCreationStrategies.forEach(strategy -> strategy.validate(context));

        Incidentstatus reportedStatus =
                incidentStatusRepository.findByName("Reported")
                        .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));

        IncidentState initialState =
                incidentStatusFactory.getState(reportedStatus.getName());

        initialState.validateInitial();
        incident.setIncidentstatus(reportedStatus);

        Incident saved = incidentRepository.save(incident);

        return incidentMapper.toDto(saved);
    }


    @Transactional
    public IncidentDetailResponseDto update(Integer incidentId, String newStatusName) {

        Incident incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new ResourceNotFoundException("Incident not found"));

        Incidentstatus targetStatus = incidentStatusRepository.findByName(newStatusName.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid incident status"));

        incidentStateTransitionHandler.transitionTo(incident, targetStatus);

        return incidentMapper.toDto(incident);
    }
}

