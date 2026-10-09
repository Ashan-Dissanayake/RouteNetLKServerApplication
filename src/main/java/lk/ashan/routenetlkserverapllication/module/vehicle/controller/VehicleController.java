package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.*;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing vehicle-related operations.
 * Provides endpoints for viewing, adding, updating, and deleting vehicles.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "Endpoints for managing vehicles in the system.")
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Retrieves a list of vehicles. If parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of search parameters.
     * @return A response entity containing a list of vehicle details and the total count.
     */
    @Operation(summary = "Get all vehicles or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved vehicles"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'vehicle-view' authority")
    })
    @PreAuthorize("hasAuthority('vehicle-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<VehicleDetailResponseDto> vehicles = params.isEmpty()
                ? vehicleService.getVehicles()
                : vehicleService.searchVehicle(params);
        return APIResponseBuilder.list(vehicles, vehicles.size());
    }

    /**
     * Retrieves a summary of all vehicles.
     *
     * @return A response entity containing a list of vehicle summaries and the total count.
     */
    @Operation(summary = "Get vehicle summaries for dropdowns", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleSummaryDto>>> get() {
        List<VehicleSummaryDto> vehicleSummaries = vehicleService.getVehicleSummary();
        return APIResponseBuilder.list(vehicleSummaries, vehicleSummaries.size());
    }

    /**
     * Adds a new vehicle.
     *
     * @param vehicleCreateRequest The details of the vehicle to be created.
     * @return A response entity containing the details of the created vehicle.
     */
    @Operation(summary = "Add a new vehicle", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'vehicle-add' authority")
    })
    @PreAuthorize("hasAuthority('vehicle-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<VehicleDetailResponseDto>> add(
            @RequestBody @Valid VehicleCreateRequestDto vehicleCreateRequest)
    {
        VehicleDetailResponseDto savedVehicle = vehicleService.createVehicle(vehicleCreateRequest);
        return APIResponseBuilder.list(savedVehicle, savedVehicle.getId());
    }

    /**
     * Updates an existing vehicle.
     *
     * @param vehicleUpdateRequestDto The updated details of the vehicle.
     * @return A response entity containing the details of the updated vehicle.
     */
    @Operation(summary = "Update an existing vehicle", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'vehicle-update' authority"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @PreAuthorize("hasAuthority('vehicle-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<VehicleDetailResponseDto>> update(
            @RequestBody @Valid VehicleUpdateRequestDto vehicleUpdateRequestDto)
    {
        VehicleDetailResponseDto updatedVehicle = vehicleService.updateVehicle(vehicleUpdateRequestDto);
        return APIResponseBuilder.updated(updatedVehicle, updatedVehicle.getId());
    }

    /**
     * Deactivates a list of vehicles.
     *
     * @param ids A list of vehicle IDs to be deactivated.
     * @return A response entity containing the IDs of the deactivated vehicles.
     */
    @Operation(summary = "Deactivate multiple vehicles", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicles deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'vehicle-delete' authority")
    })
    @PreAuthorize("hasAuthority('vehicle-delete')")
    @DeleteMapping
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(
            @RequestBody List<Integer> ids
    ) {
        List<Integer> deactivatedIds = vehicleService.deactivateVehicle(ids);
        return APIResponseBuilder.deleted(deactivatedIds);
    }

}
