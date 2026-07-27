package lk.ashan.routenetlkserverapllication.module.trip.controller;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
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
 * Controller for managing trip statuses.
 * Provides endpoints to retrieve trip status summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/trip-statuses")
@RequiredArgsConstructor
public class TripStatusController {

    private final TripStatusService tripStatusService;

    /**
     * Retrieves a list of trip status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of TripStatusDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripStatusDto>>> get() {
        List<TripStatusDto> tripStatuses = tripStatusService.getTripStatuses();
        return APIResponseBuilder.list(tripStatuses, tripStatuses.size());
    }

}
