package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ModelDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.ModelService;
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
 * Controller for managing vehicle models.
 * Provides endpoints for retrieving model summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    /**
     * Retrieves a list of vehicle model summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of ModelDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ModelDto>>> get() {
        List<ModelDto> models = modelService.getModels();
        return APIResponseBuilder.list(models, models.size());
    }

}
