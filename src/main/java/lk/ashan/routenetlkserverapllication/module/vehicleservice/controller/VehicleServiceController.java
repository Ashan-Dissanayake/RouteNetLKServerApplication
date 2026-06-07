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
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-service")
@RequiredArgsConstructor
public class VehicleServiceController {

    private final VehicleServiceIdentificationService vehicleServiceIdentificationService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServiceDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<VehicleServiceDetailResponseDto> vehicleServices = params.isEmpty()
                ?vehicleServiceIdentificationService.getVehicleServices()
                : vehicleServiceIdentificationService.searchVehicleService(params);

        return APIResponseBuilder.list(vehicleServices, vehicleServices.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> add(
            @RequestBody @Valid VehicleServiceCreateRequestDto vehicleServiceCreateRequestDto)
    {
        VehicleServiceDetailResponseDto savedVehicleService = vehicleServiceIdentificationService
                .createVehicleService(vehicleServiceCreateRequestDto);
        return APIResponseBuilder.created(savedVehicleService, savedVehicleService.getId());
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> startExecution(
            @PathVariable Integer id,
            @Valid @RequestBody VehicleServiceStartRequestDto executionPayload
    ) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.startExecution(id, executionPayload));
    }

    @PostMapping("/{id}/hold-parts")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> placeOnHold(@PathVariable Integer id) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.placeOnHold(id));
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<APISuccessResponse<VehicleServiceDetailResponseDto>> complete(
            @PathVariable Integer id,
            @Valid @RequestBody VehicleServiceCompleteRequestDto completePayload
    ) {
        return APIResponseBuilder.ok(vehicleServiceIdentificationService.complete(id, completePayload));
    }

}
