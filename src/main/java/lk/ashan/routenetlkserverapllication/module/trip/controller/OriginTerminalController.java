package lk.ashan.routenetlkserverapllication.module.trip.controller;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OriginTerminalDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.OriginTerminalService;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripTypeService;
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
 * Controller for managing origin terminals.
 * Provides endpoints to retrieve origin terminal data.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/origin-terminals")
@RequiredArgsConstructor
public class OriginTerminalController {

    private final OriginTerminalService originTerminalService;

    /**
     * Retrieves a list of origin terminal summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of OriginTerminalDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<OriginTerminalDto>>> get() {
        List<OriginTerminalDto> originTerminals = originTerminalService.getOriginTerminals();
        return APIResponseBuilder.list(originTerminals, originTerminals.size());
    }

}
