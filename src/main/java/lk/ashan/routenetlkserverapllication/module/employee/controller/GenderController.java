package lk.ashan.routenetlkserverapllication.module.employee.controller;

import lk.ashan.routenetlkserverapllication.module.employee.model.dto.GenderDto;
import lk.ashan.routenetlkserverapllication.module.employee.service.GenderService;
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
 * Controller for managing gender-related operations.
 * Provides endpoints for retrieving gender summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/genders")
@RequiredArgsConstructor
public class GenderController {

    private final GenderService genderService;

    /**
     * Retrieves a list of gender summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of GenderDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GenderDto>>> get() {
        List<GenderDto> genders = genderService.getGenders();
        return APIResponseBuilder.list(genders, genders.size());
    }

}
