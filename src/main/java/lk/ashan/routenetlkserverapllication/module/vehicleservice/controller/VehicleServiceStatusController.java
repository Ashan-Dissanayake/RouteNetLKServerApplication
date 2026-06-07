package lk.ashan.routenetlkserverapllication.module.vehicleservice.controller;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceStatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.service.VehicleServiceStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-service-statuses")
@RequiredArgsConstructor
public class VehicleServiceStatusController {

    private final VehicleServiceStatusService vehicleServiceStatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServiceStatusDto>>> get() {
        List<VehicleServiceStatusDto> vehicleServiceStatus = vehicleServiceStatusService.getVehicleServiceStatus();
        return APIResponseBuilder.list(vehicleServiceStatus, vehicleServiceStatus.size());
    }

}
