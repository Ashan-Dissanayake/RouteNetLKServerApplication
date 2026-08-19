package lk.ashan.routenetlkserverapllication.module.vehicleservice.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceCompleteRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceStartRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.service.VehicleServiceIdentificationService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing vehicle services.
 * Provides endpoints for viewing, adding, starting, holding, and completing vehicle services.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-services")
@RequiredArgsConstructor
public class VehicleServiceController {

    private final VehicleServiceIdentificationService vehicleServiceIdentificationService;

    /**
     * Retrieves a list of vehicle services. If parameters are provided, it performs a search.
     *
     * @param params A map of query parameters for filtering vehicle services.
     * @return A ResponseEntity containing a list of vehicle services and their details.
     */
    @PreAuthorize("hasAuthority('vehicle-service-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServiceDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<VehicleServiceDetailResponseDto> vehicleServices = params.isEmpty()
                ? vehicleServiceIdentificationService.getVehicleServices()
                : vehicleServiceIdentificationService.searchVehicleService(params);

        return APIResponseBuilder.list(vehicleServices, vehicleServices.size());
    }

    /**
     * Adds a new vehicle service.
     *
     * @param vehicleServiceCreateRequestDto The DTO containing details for creating a new vehicle service.
     * @return A ResponseEntity containing the created vehicle service details.
     */
    @PreAuthorize("hasAuthority('vehicle-service-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> add(
            @RequestBody @Valid VehicleServiceCreateRequestDto vehicleServiceCreateRequestDto)
    {
        VehicleServiceDetailResponseDto savedVehicleService = vehicleServiceIdentificationService
                .createVehicleService(vehicleServiceCreateRequestDto);
        return APIResponseBuilder.created(savedVehicleService, savedVehicleService.getId());
    }

    /**
     * Starts the execution of a vehicle service.
     *
     * @param id The ID of the vehicle service to start.
     * @param executionPayload The DTO containing execution details.
     * @return A ResponseEntity containing the updated vehicle service details.
     */
    @PreAuthorize("hasAuthority('vehicle-service-start')")
    @PostMapping("/{id}/start")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> startExecution(
            @PathVariable Integer id,
            @Valid @RequestBody VehicleServiceStartRequestDto executionPayload
    ) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.startExecution(id, executionPayload));
    }

    /**
     * Places a vehicle service on hold.
     *
     * @param id The ID of the vehicle service to place on hold.
     * @return A ResponseEntity containing the updated vehicle service details.
     */
    @PreAuthorize("hasAuthority('vehicle-service-hold')")
    @PostMapping("/{id}/hold-parts")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> placeOnHold(
            @PathVariable Integer id
    ) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.placeOnHold(id));
    }

    /**
     * Completes a vehicle service.
     *
     * @param id The ID of the vehicle service to complete.
     * @param completePayload The DTO containing completion details.
     * @return A ResponseEntity containing the updated vehicle service details.
     */
    @PreAuthorize("hasAuthority('vehicle-service-complete')")
    @PostMapping("/{id}/complete")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> complete(
            @PathVariable Integer id,
            @Valid @RequestBody VehicleServiceCompleteRequestDto completePayload
    ) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.complete(id, completePayload));
    }

}
