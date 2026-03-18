package lk.ashan.routenetlkserverapllication.module.trip.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.OverrideSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/trips")
@RequiredArgsConstructor
public class TripController {
    
    private final TripService tripService;

    @PreAuthorize("hasAuthority('trip-select')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripDetailResponseDto> trips = params.isEmpty()
                ? tripService.getTrips()
                : tripService.searchTrips(params);
        return APIResponseBuilder.list(trips, trips.size());
    }

    @PreAuthorize("hasAuthority('trip-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> createTrip(
            @RequestBody @Valid TripCreateRequestDto createRequestDto
            ){
        TripDetailResponseDto savedTrip = tripService.createTrip(createRequestDto);
        return APIResponseBuilder.created(savedTrip,savedTrip.getId());
    }

    @PreAuthorize("hasAuthority('trip-suggest-override')")
    @PostMapping("/{tripId}/override/suggest")
    public ResponseEntity<APISuccessResponse<OverrideSuggestionResponse>> suggestOverride(
            @PathVariable Integer tripId) {
        OverrideSuggestionResponse response = tripService.triggerOverrideSolver(tripId);
        return APIResponseBuilder.ok(
                response,
                Map.of("action", "suggestion_generated")
        );    }

    @PreAuthorize("hasAuthority('trip-approve-override')")
    @PostMapping("/{tripId}/approve-override")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> approveOverride(
            @PathVariable Integer tripId,
            @RequestParam Integer vehicleId) {

        TripDetailResponseDto response =
                tripService.approveOverride(tripId, vehicleId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "override_approved")
        );
    }

    @PreAuthorize("hasAuthority('trip-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> updateTrip(
            @RequestBody @Valid TripUpdateRequestDto updateRequestDto
    ){
        TripDetailResponseDto updatedTrip = tripService.updateTrip(updateRequestDto);
        return APIResponseBuilder.updated(updatedTrip, updatedTrip.getId());
    }

    @PreAuthorize("hasAuthority('trip-execute')")
    @PostMapping("/{tripId}/execute-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> executeTrip(
            @PathVariable Integer tripId) {

        TripDetailResponseDto response =
                tripService.executeTrip(tripId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_executed", "status", response.getTripstatus().getName())
        );
    }

    @PreAuthorize("hasAuthority('trip-cancel')")
    @PostMapping("/{tripId}/cancel-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> cancelTrip(
            @PathVariable Integer tripId) {

        TripDetailResponseDto response =
                tripService.cancelTrip(tripId);

        System.out.println("Trip " + tripId + " cancelled. Current status: " + response.getTripstatus().getName());

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_cancelled", "status", "CANCELLED")
        );
    }

    @PreAuthorize("hasAuthority('trip-complete')")
    @PostMapping("/{tripId}/complete-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> completeTrip(
            @PathVariable Integer tripId,
            @RequestParam(required = false) LocalTime actualTime) {

        TripDetailResponseDto response;

        if (actualTime != null) {
            response = tripService.completeTrip(tripId, actualTime);
        } else {
            response = tripService.completeTrip(tripId);
        }

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_completed", "status", "COMPLETED")
        );
    }

}
