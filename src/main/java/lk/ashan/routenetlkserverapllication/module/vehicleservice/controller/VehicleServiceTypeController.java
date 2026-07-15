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

@CrossOrigin
@RestController
@RequestMapping(value = "/vehicle-service-types")
@RequiredArgsConstructor
public class VehicleServiceTypeController {

    private final VehicleServiceTypeService vehicleServiceTypeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServiceTypeDto>>> get() {
        List<VehicleServiceTypeDto> vehicleServiceType = vehicleServiceTypeService.getVehicleServiceTypes();
        return APIResponseBuilder.list(vehicleServiceType, vehicleServiceType.size());
    }

}
