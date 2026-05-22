package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.controller;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service.IncidentVehicleAllocationStatusService;
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
@RequestMapping(value = "/incident-vehicle-allocation-statuses")
@RequiredArgsConstructor
public class IncidentVehicleAllocationStatusController {

    private final IncidentVehicleAllocationStatusService incidentVehicleAllocationStatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentVehicleAllocationStatusDto>>> get() {
        List<IncidentVehicleAllocationStatusDto> incidentVehicleAllocationStatuses = incidentVehicleAllocationStatusService.getIncidentVehicleAllocationStatuses();
        return APIResponseBuilder.list(incidentVehicleAllocationStatuses, incidentVehicleAllocationStatuses.size());
    }

}
