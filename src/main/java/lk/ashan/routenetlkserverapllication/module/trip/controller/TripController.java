package lk.ashan.routenetlkserverapllication.module.trip.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

/**
 * Controller for managing trip-related operations.
 * Provides endpoints for creating, retrieving, activating, suspending, and discontinuing trips.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/trips")
@RequiredArgsConstructor
@Tag(name = "Trip Management", description = "Endpoints for managing transport trips, including status changes.")
public class TripController {

    private final TripService tripService;

    /**
     * Retrieves a list of trips. If parameters are provided, it performs a search based on the parameters.
     *
     * @param params a map of query parameters for filtering trips
     * @return a response entity containing a list of trip details
     */
    @Operation(summary = "Get all trips or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved trips"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'trip-view' authority")
    })
    @PreAuthorize("hasAuthority('trip-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<TripDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<TripDetailResponseDto> trips = params.isEmpty() ? tripService.getTrips() : tripService.searchTrips(params);
        return APIResponseBuilder.list(trips, trips.size());
    }

    /**
     * Creates a new trip.
     *
     * @param createRequestDto the request DTO containing trip creation details
     * @return a response entity containing the created trip details
     */
    @Operation(summary = "Add a new trip", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Trip created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'trip-add' authority")
    })
    @PreAuthorize("hasAuthority('trip-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> createTrip(
            @Valid  @RequestBody TripCreateRequestDto createRequestDto
    ){
        TripDetailResponseDto savedTrip = tripService.createTrip(createRequestDto);
        return APIResponseBuilder.created(savedTrip,savedTrip.getId());
    }

    /**
     * Activates a trip.
     *
     * @param tripId the ID of the trip to activate
     * @return a response entity containing the activated trip details
     */
    @Operation(summary = "Activate a trip", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip activated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'trip-activate' authority"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @PreAuthorize("hasAuthority('trip-activate')")
    @PostMapping("/{tripId}/activate-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> activate(
            @PathVariable Integer tripId
    ) {
        TripDetailResponseDto response = tripService.activateTrip(tripId);
        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_activated", "status", response.getTripstatus().getName())
        );
    }

    /**
     * Suspends a trip.
     *
     * @param tripId the ID of the trip to suspend
     * @return a response entity containing the suspended trip details
     */
    @Operation(summary = "Suspend a trip", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip suspended successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'trip-suspend' authority"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @PreAuthorize("hasAuthority('trip-suspend')")
    @PostMapping("/{tripId}/suspend-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> suspend(
            @PathVariable Integer tripId
    ) {
        TripDetailResponseDto response = tripService.suspendTrip(tripId);
        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_suspended", "status", response.getTripstatus().getName())
        );
    }

    /**
     * Discontinues a trip.
     *
     * @param tripId the ID of the trip to discontinue
     * @return a response entity containing the discontinued trip details
     */
    @Operation(summary = "Discontinue a trip", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trip discontinued successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'trip-discontinue' authority"),
            @ApiResponse(responseCode = "404", description = "Trip not found")
    })
    @PreAuthorize("hasAuthority('trip-discontinue')")
    @PostMapping("/{tripId}/discontinue-trip")
    public ResponseEntity<APISuccessResponse<TripDetailResponseDto>> discontinue(
            @PathVariable Integer tripId
    ) {
        TripDetailResponseDto response = tripService.discontinueTrip(tripId);
        return APIResponseBuilder.ok(
                response,
                Map.of("action", "trip_discontinued", "status", response.getTripstatus().getName())
        );
    }
}
