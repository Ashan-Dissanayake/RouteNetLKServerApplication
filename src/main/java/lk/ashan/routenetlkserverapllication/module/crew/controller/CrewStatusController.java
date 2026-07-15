package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.CrewStatusDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.CrewStatusService;
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
 * Controller for managing crew statuses.
 * Provides endpoints to retrieve crew status summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/crew-statuses")
@RequiredArgsConstructor
public class CrewStatusController {

    private final CrewStatusService crewStatusService;

    /**
     * Retrieves a list of crew status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of CrewStatusDto objects
     * @throws RuntimeException if an error occurs while fetching crew statuses
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<CrewStatusDto>>> get() {
        List<CrewStatusDto> crewStatuses = crewStatusService.getCrewStatuses();
        return APIResponseBuilder.list(crewStatuses, crewStatuses.size());
    }

}
