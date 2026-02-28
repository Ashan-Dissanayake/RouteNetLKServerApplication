package lk.ashan.routenetlkserverapllication.module.incident.service;

import jakarta.validation.constraints.NotNull;
import lk.ashan.routenetlkserverapllication.module.incident.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.mapper.IncidentMapper;
import lk.ashan.routenetlkserverapllication.module.incident.model.Incident;
import lk.ashan.routenetlkserverapllication.module.incident.repository.IncidentRepository;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.Trip;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final IncidentMapper incidentMapper;

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
}

