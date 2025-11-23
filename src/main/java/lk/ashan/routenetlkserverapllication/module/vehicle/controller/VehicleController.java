package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.dto.VehicleDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<VehicleDetailResponseDto> vehicles = params.isEmpty()
                ?vehicleService.getVehicles()
                : vehicleService.searchVehicle(params);
        return APIResponseBuilder.getResponse(vehicles, vehicles.size());
    }
}
