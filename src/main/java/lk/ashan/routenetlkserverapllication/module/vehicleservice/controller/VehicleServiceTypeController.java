package lk.ashan.routenetlkserverapllication.module.vehicleservice.controller;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.service.VehicleServiceTypeService;
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
 * Controller for managing vehicle service types.
 * Provides endpoints for retrieving vehicle service type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-service-types")
@RequiredArgsConstructor
public class VehicleServiceTypeController {

    private final VehicleServiceTypeService vehicleServiceTypeService;

    /**
     * Retrieves a list of vehicle service type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of VehicleServiceTypeDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServiceTypeDto>>> get() {
        List<VehicleServiceTypeDto> vehicleServiceType = vehicleServiceTypeService.getVehicleServiceTypes();
        return APIResponseBuilder.list(vehicleServiceType, vehicleServiceType.size());
    }

}
