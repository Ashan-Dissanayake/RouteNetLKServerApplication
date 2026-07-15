package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.RegionalOfficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.RegionalOfficeService;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Controller for managing regional office-related operations.
 * Provides endpoints for retrieving regional office summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/regional-offices")
@RequiredArgsConstructor
public class RegionalOfficeController {

    private final RegionalOfficeService regionalOfficeService;

    /**
     * Retrieves a list of regional office summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of RegionalOfficeDto objects
     *         and the total count of regional offices.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RegionalOfficeDto>>> get() {
        List<RegionalOfficeDto> regionalOffices = regionalOfficeService.getRegionalOffices();
        return APIResponseBuilder.list(regionalOffices, regionalOffices.size());
    }

}
