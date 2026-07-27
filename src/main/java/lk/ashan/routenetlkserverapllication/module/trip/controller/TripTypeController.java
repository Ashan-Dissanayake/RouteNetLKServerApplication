package lk.ashan.routenetlkserverapllication.module.trip.controller;

import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripStatusDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripTypeDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripStatusService;
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
 * Controller for managing trip types.
 * Provides endpoints for retrieving trip type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/trip-types")
@RequiredArgsConstructor
public class TripTypeController {

    private final TripTypeService tripTypeService;

    /**
     * Retrieves a list of trip type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of TripTypeDto objects
     *         and the total count of trip types.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripTypeDto>>> get() {
        List<TripTypeDto> tripTypes = tripTypeService.getTripTypes();
        return APIResponseBuilder.list(tripTypes, tripTypes.size());
    }

}
