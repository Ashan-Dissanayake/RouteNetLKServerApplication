package lk.ashan.routenetlkserverapllication.module.incident.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentSummaryDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentService;
import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartSummaryDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @PreAuthorize("hasAuthority('incident-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<IncidentDetailResponseDto> incidents = params.isEmpty()
                ? incidentService.getIncidents()
                : incidentService.searchIncidents(params);
        return APIResponseBuilder.list(incidents, incidents.size());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentSummaryDto>>> get() {
        List<IncidentSummaryDto> incidents =  incidentService.getSummaryIncidents();
        return APIResponseBuilder.list(incidents, incidents.size());
    }

    @PreAuthorize("hasAuthority('incident-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> create(
            @RequestBody @Valid IncidentCreateRequestDto createRequestDto
    ) {
        IncidentDetailResponseDto savedIncident = incidentService.create(createRequestDto);
        return APIResponseBuilder.created(savedIncident,savedIncident.getId());
    }

    @PreAuthorize("hasAuthority('incident-start')")
    @PostMapping("/{id}/in-progress")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> inProgress(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.inProgress(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    @PreAuthorize("hasAuthority('incident-vehicle-recovery')")
    @PostMapping("/{id}/vehicle-recovery")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> vehicleRecovery(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.vehicleRecovery(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    @PreAuthorize("hasAuthority('incident-pending-allocation')")
    @PostMapping("/{id}/pending-allocation")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> pendingAllocation(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.pendingAllocation(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    @PreAuthorize("hasAuthority('incident-resolve')")
    @PostMapping("/{id}/resolved")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> resolved(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.resolved(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    @PreAuthorize("hasAuthority('incident-close')")
    @PostMapping("/{id}/closed")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> closed(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.closed(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

}
