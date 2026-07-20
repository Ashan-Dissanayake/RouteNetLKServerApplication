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

@CrossOrigin
@RestController
@RequestMapping(value = "/incident-vehicle-allocations")
@RequiredArgsConstructor
public class IncidentVehicleAllocationController {

    private final IncidentVehicleAllocationService allocationService;

    @PreAuthorize("hasAuthority('incident-vehicle-allocation-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentVehicleAllocationDetailsResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<IncidentVehicleAllocationDetailsResponseDto> incidentVhicleAllocations = params.isEmpty()
                ? allocationService.getIncidentVehicleAllocations()
                : allocationService.searchIncidentAllocations(params);
        return APIResponseBuilder.list(incidentVhicleAllocations, incidentVhicleAllocations.size());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocation-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> createAllocation(
            @Valid @RequestBody IncidentVehicleAllocationCreateRequestDto requestDto
    ) {
        IncidentVehicleAllocationDetailsResponseDto savedIncidentVehicleAllocation =
                allocationService.createAllocation(requestDto);
        return APIResponseBuilder.created(savedIncidentVehicleAllocation,savedIncidentVehicleAllocation.getId());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocation-in-progress')")
    @PostMapping("/{id}/in-progress")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> inProgress(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.inProgress(id);
        return APIResponseBuilder.ok(updated);
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocation-released')")
    @PostMapping("/{id}/released")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> released(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.released(id);
        return APIResponseBuilder.ok(updated);
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocation-cancelled')")
    @PostMapping("/{id}/cancelled")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> cancelled(
            @PathVariable Integer id
    ) {
        IncidentVehicleAllocationDetailsResponseDto updated = allocationService.cancelled(id);
        return APIResponseBuilder.ok(updated);
    }

}
