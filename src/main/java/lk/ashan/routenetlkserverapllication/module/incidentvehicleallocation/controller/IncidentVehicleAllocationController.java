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

    @PreAuthorize("hasAuthority('incident-vehicle-allocations-select')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentVehicleAllocationDetailsResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<IncidentVehicleAllocationDetailsResponseDto> incidents = params.isEmpty()
                ? allocationService.getIncidentVehicleAllocations()
                : allocationService.searchIncidentAllocations(params);
        return APIResponseBuilder.list(incidents, incidents.size());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocations-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> createAllocation(
            @Valid @RequestBody IncidentVehicleAllocationCreateRequestDto requestDto
    ) {
        IncidentVehicleAllocationDetailsResponseDto savedIncidentVehicleAllocation =
                allocationService.createAllocation(requestDto);

        return APIResponseBuilder.created(savedIncidentVehicleAllocation,savedIncidentVehicleAllocation.getId());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocations-update')")
    @PutMapping("/{allocationId}/start-handling")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> startHandling(
            @PathVariable Integer allocationId
    ) {
        IncidentVehicleAllocationDetailsResponseDto updatedIncidentVehicleAllocation =
                allocationService.startHandling(allocationId);

        return APIResponseBuilder.updated(updatedIncidentVehicleAllocation,updatedIncidentVehicleAllocation.getId());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocations-update')")
    @PutMapping("/{allocationId}/release")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> releaseAllocation(
            @PathVariable Integer allocationId
    ) {
        IncidentVehicleAllocationDetailsResponseDto updatedIncidentVehicleAllocation =
                allocationService.releaseAllocation(allocationId);

        return APIResponseBuilder.updated(updatedIncidentVehicleAllocation,updatedIncidentVehicleAllocation.getId());
    }

    @PreAuthorize("hasAuthority('incident-vehicle-allocations-update')")
    @PutMapping("/{allocationId}/cancel")
    public ResponseEntity<APISuccessResponse<IncidentVehicleAllocationDetailsResponseDto>> cancelAllocation(
            @PathVariable Integer allocationId
    ) {
        IncidentVehicleAllocationDetailsResponseDto updatedIncidentVehicleAllocation =
                allocationService.cancelAllocation(allocationId);

        return APIResponseBuilder.updated(updatedIncidentVehicleAllocation,updatedIncidentVehicleAllocation.getId());
    }
}
