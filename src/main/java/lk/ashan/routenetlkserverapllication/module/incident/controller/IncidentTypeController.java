package lk.ashan.routenetlkserverapllication.module.incident.controller;

import lk.ashan.routenetlkserverapllication.module.incident.model.dto.IncidentTypeDto;
import lk.ashan.routenetlkserverapllication.module.incident.service.IncidentTypeService;
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

@CrossOrigin
@RestController
@RequestMapping(value = "/incident-types")
@RequiredArgsConstructor
public class IncidentTypeController {

    private final IncidentTypeService incidentTypeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentTypeDto>>> get() {
        List<IncidentTypeDto> incidentTypes = incidentTypeService.getIncidentTypes();
        return APIResponseBuilder.list(incidentTypes, incidentTypes.size());
    }

}
