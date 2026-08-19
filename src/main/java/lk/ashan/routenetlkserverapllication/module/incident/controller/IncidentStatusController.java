package lk.ashan.routenetlkserverapllication.module.incident.controller;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing incident statuses.
 * Provides endpoints to retrieve summaries of incident statuses.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/incident-statuses")
@RequiredArgsConstructor
public class IncidentStatusController {

    private final IncidentStatusService incidentStatusService;

    /**
     * Retrieves a list of incident status summaries.
     *
     * @return a ResponseEntity containing a success response with a list of IncidentStatusDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentStatusDto>>> get() {
        List<IncidentStatusDto> incidentStatuses = incidentStatusService.getIncidentStatuses();
        return APIResponseBuilder.list(incidentStatuses, incidentStatuses.size());
    }

}
