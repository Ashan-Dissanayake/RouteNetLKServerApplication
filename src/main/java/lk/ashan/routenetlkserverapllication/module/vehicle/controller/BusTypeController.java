package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.BusTypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.BusTypeService;
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
 * Controller for managing bus types.
 * Provides endpoints for retrieving bus type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/bus-types")
@RequiredArgsConstructor
public class BusTypeController {

    private final BusTypeService busTypeService;

    /**
     * Retrieves a list of bus type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of BusTypeDto objects.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BusTypeDto>>> get() {
        List<BusTypeDto> busTypes = busTypeService.getBusTypes();
        return APIResponseBuilder.list(busTypes, busTypes.size());
    }
}
