package lk.ashan.routenetlkserverapllication.module.trip.controller;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OpCalenderSummaryDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.OpCalenderService;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
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
 * Controller for managing operational calendars.
 * Provides endpoints for retrieving operational calendar summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/op-calenders")
@RequiredArgsConstructor
public class OpCalenderController {

    private final OpCalenderService opCalenderService;

    /**
     * Retrieves a list of operational calendar summaries.
     *
     * @return a ResponseEntity containing a list of OpCalenderSummaryDto objects
     *         wrapped in an APISuccessResponse, along with the HTTP status.
     * @throws SecurityException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<OpCalenderSummaryDto>>> get() {
        List<OpCalenderSummaryDto> opCalenders = opCalenderService.getOpCalenders();
        return APIResponseBuilder.list(opCalenders, opCalenders.size());
    }


}
