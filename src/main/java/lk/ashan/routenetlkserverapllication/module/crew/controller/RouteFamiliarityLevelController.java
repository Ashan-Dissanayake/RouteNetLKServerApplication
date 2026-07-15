package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.RouteFamiliarityLevelService;
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
 * Controller for managing route familiarity levels.
 * Provides endpoints to retrieve route familiarity level summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/route-familiarity-levels")
@RequiredArgsConstructor
public class RouteFamiliarityLevelController {

    private final RouteFamiliarityLevelService routeFamiliarityLevelService;

    /**
     * Retrieves a list of route familiarity level summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of RouteFamiliarityLevelDto objects
     * @throws RuntimeException if an error occurs while fetching the data
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RouteFamiliarityLevelDto>>> get() {
        List<RouteFamiliarityLevelDto> routeFamiliarityLevels = routeFamiliarityLevelService.getRouteFamiliarityLevels();
        return APIResponseBuilder.list(routeFamiliarityLevels, routeFamiliarityLevels.size());
    }

}
