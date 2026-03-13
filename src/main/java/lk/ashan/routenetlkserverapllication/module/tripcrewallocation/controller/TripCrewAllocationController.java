package lk.ashan.routenetlkserverapllication.module.tripcrewallocation.controller;

import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.dto.*;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.model.dto.TripCrewAllocationSuggestionResponseDto;
import lk.ashan.routenetlkserverapllication.module.tripcrewallocation.service.TripCrewAllocationService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/trip-crew-allocations")
@RequiredArgsConstructor
@Slf4j
public class TripCrewAllocationController {

    private final TripCrewAllocationService tripCrewAllocationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripCrewAllocationDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripCrewAllocationDetailResponseDto> trips = params.isEmpty()
                ? tripCrewAllocationService.getTripAllocations()
                : tripCrewAllocationService.searchTripCrewAllocations(params);
        return APIResponseBuilder.list(trips, trips.size());
    }

    /**
     * POST /trip-crew-allocations/{tripId}/generate
     * Generate AI-powered crew suggestions using OptaPlanner
     */
    @PostMapping(value = "/{tripId}/generate", produces = "application/json")
    public ResponseEntity<APISuccessResponse<TripCrewAllocationSuggestionResponseDto>> generateSuggestions(
            @PathVariable Integer tripId
    ) {
        log.info("POST /trip-crew-allocations/{}/generate", tripId);

        TripCrewAllocationSuggestionResponseDto response =
                tripCrewAllocationService.generateSuggestions(tripId);

        return APIResponseBuilder.created(response, tripId);
    }

    /**
     * PUT /trip-crew-allocations/{id}/approve
     * Approve a suggested allocation (SUGGESTED → CONFIRMED)
     */
    @PutMapping(value = "/{id}/approve", produces = "application/json")
    public ResponseEntity<APISuccessResponse<TripCrewAllocationDetailResponseDto>> approve(
            @PathVariable Integer id
    ) {
        log.info("PUT /trip-crew-allocations/{}/approve", id);

        TripCrewAllocationDetailResponseDto approved =
                tripCrewAllocationService.approveSuggestion(id);

        return APIResponseBuilder.updated(approved, id);
    }

    /**
     * PUT /trip-crew-allocations/{id}/reject
     * Reject a suggested allocation (SUGGESTED → REJECTED)
     */
    @PutMapping(value = "/{id}/reject", produces = "application/json")
    public ResponseEntity<APISuccessResponse<TripCrewAllocationDetailResponseDto>> reject(
            @PathVariable Integer id
    ) {
        log.info("PUT /trip-crew-allocations/{}/reject", id);

        TripCrewAllocationDetailResponseDto rejected =
                tripCrewAllocationService.rejectSuggestion(id);

        return APIResponseBuilder.updated(rejected, id);
    }

    @DeleteMapping(value = "/rejected/{tripId}")
    public ResponseEntity<APISuccessResponse<Void>> clearRejected(
            @PathVariable Integer tripId
    ) {
        log.info("DELETE /trip-crew-allocations/rejected/{}", tripId);

        tripCrewAllocationService.clearRejectedAllocations(tripId);

        return APIResponseBuilder.deleted(tripId);
    }

}
