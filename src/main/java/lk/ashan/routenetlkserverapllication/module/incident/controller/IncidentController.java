package lk.ashan.routenetlkserverapllication.module.incident.controller;


import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<IncidentDetailResponseDto> incidents = params.isEmpty()
                ? incidentService.getIncidents()
                : incidentService.searchIncidents(params);
        return APIResponseBuilder.list(incidents, incidents.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> create(
            @RequestBody IncidentCreateRequestDto createRequestDto
    ) {
        IncidentDetailResponseDto savedIncident = incidentService.create(createRequestDto);
        return APIResponseBuilder.created(savedIncident,savedIncident.getId());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> changeStatus(
            @PathVariable Integer id,
            @RequestParam String status
    ) {
        IncidentDetailResponseDto updatedIncident =
                incidentService.update(id, status);
        return APIResponseBuilder.updated(updatedIncident,updatedIncident.getId());
    }
}
