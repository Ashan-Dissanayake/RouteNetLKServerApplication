package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.DesignationDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.DesignationService;
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
 * Controller for managing designations.
 * Provides endpoints for retrieving designation summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/designations")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService designationService;

    /**
     * Retrieves a list of designation summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of DesignationDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<DesignationDto>>> get() {
        List<DesignationDto> designations = designationService.getDesignations();
        return APIResponseBuilder.list(designations, designations.size());
    }

}
