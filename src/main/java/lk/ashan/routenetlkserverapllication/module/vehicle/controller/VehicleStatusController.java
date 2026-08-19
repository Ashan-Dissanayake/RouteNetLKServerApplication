package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.VehiclestatusDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.VehicleStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing vehicle statuses.
 * Provides endpoints to retrieve vehicle status summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-statuses")
@RequiredArgsConstructor
public class VehicleStatusController {

    private final VehicleStatusService vehicleStatusService;

    /**
     * Retrieves a list of vehicle status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of VehiclestatusDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehiclestatusDto>>> get() {
        List<VehiclestatusDto> vehicleStatuses = vehicleStatusService.getVehicleStatuses();
        return APIResponseBuilder.list(vehicleStatuses, vehicleStatuses.size());
    }

}
