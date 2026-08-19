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

/**
 * Controller for managing incidents. Provides endpoints for creating, updating, and retrieving incidents.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    /**
     * Retrieves a list of incidents. If parameters are provided, filters the incidents based on the parameters.
     *
     * @param params A map of query parameters for filtering incidents.
     * @return A response entity containing a list of incident details.
     */
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

    /**
     * Retrieves a summary list of incidents.
     *
     * @return A response entity containing a list of incident summaries.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentSummaryDto>>> get() {
        List<IncidentSummaryDto> incidents =  incidentService.getSummaryIncidents();
        return APIResponseBuilder.list(incidents, incidents.size());
    }

    /**
     * Creates a new incident.
     *
     * @param createRequestDto The data transfer object containing the details of the incident to be created.
     * @return A response entity containing the details of the created incident.
     */
    @PreAuthorize("hasAuthority('incident-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> create(
            @RequestBody @Valid IncidentCreateRequestDto createRequestDto
    ) {
        IncidentDetailResponseDto savedIncident = incidentService.create(createRequestDto);
        return APIResponseBuilder.created(savedIncident,savedIncident.getId());
    }

    /**
     * Updates the status of an incident to "in-progress".
     *
     * @param id The ID of the incident to be updated.
     * @return A response entity containing the updated incident details.
     */
    @PreAuthorize("hasAuthority('incident-start')")
    @PostMapping("/{id}/in-progress")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> inProgress(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.inProgress(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    /**
     * Updates the status of an incident to "vehicle recovery".
     *
     * @param id The ID of the incident to be updated.
     * @return A response entity containing the updated incident details.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-recovery')")
    @PostMapping("/{id}/vehicle-recovery")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> vehicleRecovery(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.vehicleRecovery(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    /**
     * Updates the status of an incident to "pending allocation".
     *
     * @param id The ID of the incident to be updated.
     * @return A response entity containing the updated incident details.
     */
    @PreAuthorize("hasAuthority('incident-pending-allocation')")
    @PostMapping("/{id}/pending-allocation")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> pendingAllocation(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.pendingAllocation(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    /**
     * Updates the status of an incident to "resolved".
     *
     * @param id The ID of the incident to be updated.
     * @return A response entity containing the updated incident details.
     */
    @PreAuthorize("hasAuthority('incident-resolve')")
    @PostMapping("/{id}/resolved")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> resolved(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.resolved(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

    /**
     * Updates the status of an incident to "closed".
     *
     * @param id The ID of the incident to be updated.
     * @return A response entity containing the updated incident details.
     */
    @PreAuthorize("hasAuthority('incident-close')")
    @PostMapping("/{id}/closed")
    public ResponseEntity<APISuccessResponse<IncidentDetailResponseDto>> closed(
            @PathVariable Integer id
    ) {
        IncidentDetailResponseDto updatedIncident = incidentService.closed(id);
        return APIResponseBuilder.ok(updatedIncident);
    }

}
