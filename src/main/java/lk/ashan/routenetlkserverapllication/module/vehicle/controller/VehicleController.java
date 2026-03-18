package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehicleUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleService;
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
@RequestMapping(value = "/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PreAuthorize("hasAuthority('vehicle-select')")
    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<VehicleDetailResponseDto> vehicles = params.isEmpty()
                ?vehicleService.getVehicles()
                : vehicleService.searchVehicle(params);
        return APIResponseBuilder.list(vehicles, vehicles.size());
    }

    @PreAuthorize("hasAuthority('vehicle-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<VehicleDetailResponseDto>> add(
            @RequestBody @Valid VehicleCreateRequestDto vehicleCreateRequest)
    {
        VehicleDetailResponseDto savedVehicle = vehicleService.createVehicle(vehicleCreateRequest);
        return APIResponseBuilder.list(savedVehicle, savedVehicle.getId());
    }

    @PreAuthorize("hasAuthority('vehicle-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<VehicleDetailResponseDto>> update(
            @RequestBody @Valid VehicleUpdateRequestDto vehicleUpdateRequestDto)
    {
        VehicleDetailResponseDto updatedVehicle = vehicleService.updateVehicle(vehicleUpdateRequestDto);
        return APIResponseBuilder.updated(updatedVehicle,updatedVehicle.getId());
    }

    @PreAuthorize("hasAuthority('vehicle-delete')")
    @PostMapping("/deactivate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(@RequestBody List<Integer> ids) {
        List<Integer> deactivatedIds = vehicleService.deactivateVehicle(ids);
        return APIResponseBuilder.deleted(deactivatedIds);
    }

}
