package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.model.dto.LicenseCategoryDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.LicenseCategoryService;
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
 * Controller for managing license categories.
 * Provides endpoints to retrieve license category summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/license-categories")
@RequiredArgsConstructor
public class LicenseCategoryController {

    private final LicenseCategoryService licenseCategoryService;

    /**
     * Retrieves a list of license category summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of LicenseCategoryDto objects
     * @throws RuntimeException if an error occurs while fetching license categories
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<LicenseCategoryDto>>> get() {
        List<LicenseCategoryDto> licenseCategories = licenseCategoryService.getLicenseCategories();
        return APIResponseBuilder.list(licenseCategories, licenseCategories.size());
    }

}
