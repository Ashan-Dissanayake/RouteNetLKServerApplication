package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.FueltypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.FuelTypeService;
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
 * Controller for managing fuel types.
 * Provides endpoints for retrieving fuel type summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/fuel-types")
@RequiredArgsConstructor
public class FuelTypeController {

    private final FuelTypeService fuelTypeService;

    /**
     * Retrieves a list of fuel type summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of FueltypeDto objects
     *         and the total count of fuel types.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<FueltypeDto>>> get() {
        List<FueltypeDto> fuelTypes = fuelTypeService.getFuelTypes();
        return APIResponseBuilder.list(fuelTypes, fuelTypes.size());
    }

}
