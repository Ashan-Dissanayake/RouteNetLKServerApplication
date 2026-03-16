package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehiclestatusService;
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
@RequestMapping(value = "/vehicle-statuses")
@RequiredArgsConstructor
public class VehicleStatusController {

    private final VehiclestatusService vehicleStatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehiclestatusDto>>> get() {
        List<VehiclestatusDto> vehicleStatuses = vehicleStatusService.getVehiclestatuss();
        return APIResponseBuilder.list(vehicleStatuses, vehicleStatuses.size());
    }

}
