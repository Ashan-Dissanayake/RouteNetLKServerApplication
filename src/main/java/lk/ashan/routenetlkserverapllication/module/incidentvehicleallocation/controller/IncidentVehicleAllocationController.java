package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationDetailsResponseDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service.IncidentVehicleAllocationService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * Controller for managing Incident Vehicle Allocations.
 * Provides endpoints for viewing, creating, and updating the status of incident vehicle allocations.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/incident-vehicle-allocations")
@RequiredArgsConstructor
public class IncidentVehicleAllocationController {

    private final IncidentVehicleAllocationService allocationService;

    /**
     * Retrieves a list of incident vehicle allocations.
     * If query parameters are provided, performs a search based on the parameters.
     *
     * @param params A map of query parameters for filtering the results.
     * @return A ResponseEntity containing a list of IncidentVehicleAllocationDetailsResponseDto objects.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-allocation-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentVehicleAllocationDetailsResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<IncidentVehicleAllocationDetailsResponseDto> incidentVehicleAllocations = params.isEmpty()
                ? allocationService.getIncidentVehicleAllocations()
                : allocationService.searchIncidentAllocations(params);
        return APIResponseBuilder.list(incidentVehicleAllocations, incidentVehicleAllocations.size());
    }

    /**
     * Creates a new incident vehicle allocation.
     *
     * @param requestDto The request body containing the details of the allocation to be created.
     * @return A ResponseEntity containing the created IncidentVehicleAllocationDetailsResponseDto object.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-allocation-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> createAllocation(
            @Valid @RequestBody IncidentVehicleAllocationCreateRequestDto requestDto
    ) {
        IncidentVehicleAllocationDetailsResponseDto savedIncidentVehicleAllocation =
                allocationService.createAllocation(requestDto);
        return APIResponseBuilder.created(savedIncidentVehicleAllocation, savedIncidentVehicleAllocation.getId());
    }

    /**
     * Updates the status of an incident vehicle allocation to "In Progress".
     *
     * @param id The ID of the incident vehicle allocation to update.
     * @return A ResponseEntity containing the updated IncidentVehicleAllocationDetailsResponseDto object.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-allocation-in-progress')")
    @PostMapping("/{id}/in-progress")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> inProgress(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.inProgress(id);
        return APIResponseBuilder.ok(updated);
    }

    /**
     * Updates the status of an incident vehicle allocation to "Released".
     *
     * @param id The ID of the incident vehicle allocation to update.
     * @return A ResponseEntity containing the updated IncidentVehicleAllocationDetailsResponseDto object.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-allocation-released')")
    @PostMapping("/{id}/released")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> released(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.released(id);
        return APIResponseBuilder.ok(updated);
    }

    /**
     * Updates the status of an incident vehicle allocation to "Cancelled".
     *
     * @param id The ID of the incident vehicle allocation to update.
     * @return A ResponseEntity containing the updated IncidentVehicleAllocationDetailsResponseDto object.
     */
    @PreAuthorize("hasAuthority('incident-vehicle-allocation-cancelled')")
    @PostMapping("/{id}/cancelled")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> cancelled(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.cancelled(id);
        return APIResponseBuilder.ok(updated);
    }

}
