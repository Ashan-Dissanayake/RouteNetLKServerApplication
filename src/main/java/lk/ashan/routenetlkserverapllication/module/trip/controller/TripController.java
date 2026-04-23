package lk.ashan.routenetlkserverapllication.module.trip.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.model.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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


    @PreAuthorize("hasAuthority('trip-activate')")
    @PostMapping("/{tripId}/activate-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> activate(
            @PathVariable Integer tripId) {

        TripDetailResponseDto response = tripService.activateTrip(tripId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_activated", "status", response.getTripstatus().getName())
        );
    }

    @PreAuthorize("hasAuthority('trip-suspend')")
    @PostMapping("/{tripId}/suspend-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> suspend(
            @PathVariable Integer tripId) {

        TripDetailResponseDto response = tripService.suspendTrip(tripId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_suspended", "status", response.getTripstatus().getName())
        );
    }

    @PreAuthorize("hasAuthority('trip-discontinue')")
    @PostMapping("/{tripId}/discontinue-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> discontinue(
            @PathVariable Integer tripId) {

        TripDetailResponseDto response = tripService.discontinueTrip(tripId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_discontinued", "status", response.getTripstatus().getName())
        );
    }




}
