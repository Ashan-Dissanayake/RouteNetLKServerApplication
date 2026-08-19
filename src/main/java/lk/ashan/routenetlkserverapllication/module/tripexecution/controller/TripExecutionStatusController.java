package lk.ashan.routenetlkserverapllication.module.tripexecution.controller;

import lk.ashan.routenetlkserverapllication.module.tripexecution.model.dto.TripExecutionStatusDto;
import lk.ashan.routenetlkserverapllication.module.tripexecution.service.TripExecutionStatusService;
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
 * Controller for managing Trip Execution Statuses.
 * Provides endpoints to retrieve summaries of trip execution statuses.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/trip-execution-statuses")
@RequiredArgsConstructor
public class TripExecutionStatusController {

    private final TripExecutionStatusService tripExecutionStatusService;

    /**
     * Retrieves a list of trip execution status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of TripExecutionStatusDto objects
     *         and the total count of statuses.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripExecutionStatusDto>>> get() {
        List<TripExecutionStatusDto> tripExecutionStatuses = tripExecutionStatusService.getTripExecutionStatuses();
        return APIResponseBuilder.list(tripExecutionStatuses, tripExecutionStatuses.size());
    }

}
