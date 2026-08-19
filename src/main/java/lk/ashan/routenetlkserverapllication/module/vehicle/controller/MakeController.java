package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.MakeRequestDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.MakeService;
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
 * Controller for handling requests related to vehicle makes.
 * Provides endpoints for retrieving vehicle make summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/makes")
@RequiredArgsConstructor
public class MakeController {

    private final MakeService makeService;

    /**
     * Retrieves a list of vehicle make summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of MakeRequestDto objects
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<MakeRequestDto>>> get() {
        List<MakeRequestDto> makes = makeService.getMakes();
        return APIResponseBuilder.list(makes, makes.size());
    }

}
