package lk.ashan.routenetlkserverapllication.module.vehicleservice.controller;

import lk.ashan.routenetlkserverapllication.module.vehicleservice.model.dto.VehicleServicePriorityDto;
import lk.ashan.routenetlkserverapllication.module.vehicleservice.service.VehicleServicePriorityService;
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
@RequestMapping(value = "/vehicle-service-priorities")
@RequiredArgsConstructor
public class VehicleServicePriorityController {

    private final VehicleServicePriorityService vehicleServicePriorityService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<VehicleServicePriorityDto>>> get() {
        List<VehicleServicePriorityDto> vehicleServicePriority = vehicleServicePriorityService.getVehicleServicePriorities();
        return APIResponseBuilder.list(vehicleServicePriority, vehicleServicePriority.size());
    }

}
