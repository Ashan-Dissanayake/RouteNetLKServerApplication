package lk.ashan.routenetlkserverapllication.module.incident.controller;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentStatusDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/incident-statuses")
@RequiredArgsConstructor
public class IncidentStatusController {

    private final IncidentStatusService incidentStatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentStatusDto>>> get() {
        List<IncidentStatusDto> incidentStatuses = incidentStatusService.getIncidentStatuses();
        return APIResponseBuilder.list(incidentStatuses, incidentStatuses.size());
    }

}
