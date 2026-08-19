package lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.controller;

import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.model.dto.IncidentVehicleAllocationStatusDto;
import lk.ashan.routenetlkserverapllication.module.incidentvehicleallocation.service.IncidentVehicleAllocationStatusService;
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
 * Controller for managing Incident Vehicle Allocation Statuses.
 * Provides endpoints to retrieve summaries of allocation statuses.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/incident-vehicle-allocation-statuses")
@RequiredArgsConstructor
public class IncidentVehicleAllocationStatusController {

    private final IncidentVehicleAllocationStatusService incidentVehicleAllocationStatusService;

    /**
     * Retrieves a list of summaries for Incident Vehicle Allocation Statuses.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of IncidentVehicleAllocationStatusDto
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<IncidentVehicleAllocationStatusDto>>> get() {
        List<IncidentVehicleAllocationStatusDto> incidentVehicleAllocationStatuses = incidentVehicleAllocationStatusService.getIncidentVehicleAllocationStatuses();
        return APIResponseBuilder.list(incidentVehicleAllocationStatuses, incidentVehicleAllocationStatuses.size());
    }

}
