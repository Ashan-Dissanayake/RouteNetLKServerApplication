package lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.controller;

import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.model.dto.VehicleServiceSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.vehicleserviceidentification.service.VehicleServiceExecutionService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/vehicle-service-executions")
@RequiredArgsConstructor
public class VehicleServiceExecutionController {

    private final VehicleServiceExecutionService vehicleServiceExecutionService;

    @PreAuthorize("hasAuthority('vehicle-service-executions-start')")
    @PostMapping("/{scheduleId}/start")
    public ResponseEntity<APISuccessResponse<VehicleServiceSummaryResponseDto>> startService(@PathVariable Integer scheduleId) {

      VehicleServiceSummaryResponseDto response =
              vehicleServiceExecutionService.startService(scheduleId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "service_started")
        );
    }

    @PreAuthorize("hasAuthority('vehicle-service-executions-complete')")
    @PostMapping("/{scheduleId}/complete")
    public ResponseEntity<APISuccessResponse<VehicleServiceSummaryResponseDto>> completeService(@PathVariable Integer scheduleId) {

      VehicleServiceSummaryResponseDto response =
              vehicleServiceExecutionService.completeService(scheduleId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "service_completed")
        );
    }


}


