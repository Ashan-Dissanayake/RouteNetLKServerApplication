package lk.ashan.routenetlkserverapllication.module.trip.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.trip.dto.OverrideSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.trip.service.TripService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/trips")
@RequiredArgsConstructor
public class TripController {
    
    private final TripService tripService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripDetailResponseDto> trips = params.isEmpty()
                ? tripService.getTrips()
                : tripService.searchTrips(params);
        return APIResponseBuilder.getResponse(trips, trips.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> createTrip(
            @RequestBody @Valid TripCreateRequestDto createRequestDto
            ){
        TripDetailResponseDto savedTrip = tripService.createTrip(createRequestDto);
        return APIResponseBuilder.postResponse(savedTrip,savedTrip.getId());
    }

    @PostMapping("/{tripId}/suggest-override")
    public ResponseEntity<APISuccessResponse<OverrideSuggestionResponse>> suggestOverride(
            @PathVariable Integer tripId) {
        OverrideSuggestionResponse response = tripService.triggerOverrideSolver(tripId);
        return APIResponseBuilder.postResponse(response,response.getTripId());
    }

    @PostMapping("/{tripId}/approve-override")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> approveOverride(
            @PathVariable Integer tripId,
            @RequestParam Integer vehicleId) {

        TripDetailResponseDto response =
                tripService.approveOverride(tripId, vehicleId);

        return APIResponseBuilder.postResponse(response,response.getId());
    }

}
