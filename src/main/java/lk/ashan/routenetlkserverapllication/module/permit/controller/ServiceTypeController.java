package lk.ashan.routenetlkserverapllication.module.permit.controller;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.ServiceTypeDto;
import lk.ashan.routenetlkserverapllication.module.permit.service.ServiceTypeService;
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
 * Controller for managing service types.
 * Provides endpoints for retrieving service type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/service-types")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServiceTypeService serviceTypeService;

    /**
     * Retrieves a list of service type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of ServiceTypeDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ServiceTypeDto>>> get() {
        List<ServiceTypeDto> serviceTypes = serviceTypeService.getServiceTypes();
        return APIResponseBuilder.list(serviceTypes, serviceTypes.size());
    }

}
