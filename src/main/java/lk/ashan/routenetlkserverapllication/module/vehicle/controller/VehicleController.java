package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

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
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Retrieves a list of vehicles. If parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of search parameters.
     * @return A response entity containing a list of vehicle details and the total count.
     */
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
    @PreAuthorize("hasAuthority('vehicle-delete')")
    @DeleteMapping
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(
            @RequestBody List<Integer> ids
    ) {
        List<Integer> deactivatedIds = vehicleService.deactivateVehicle(ids);
        return APIResponseBuilder.deleted(deactivatedIds);
    }

}
