package lk.ashan.routenetlkserverapllication.module.grn.controller;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.service.GrnStatusService;
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
 * Controller for handling GRN (Goods Received Note) status-related operations.
 * Provides endpoints for retrieving GRN status summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/grn-statuses")
@RequiredArgsConstructor
public class GrnStatusController {

    private final GrnStatusService grnstatusService;

    /**
     * Retrieves a list of GRN status summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of GrnStatusDto objects
     *         and the total count of GRN statuses.
     * @throws org.springframework.security.access.AccessDeniedException if the user is not authenticated.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GrnStatusDto>>> get() {
        List<GrnStatusDto> grnStatuses = grnstatusService.getGrnStatuses();
        return APIResponseBuilder.list(grnStatuses, grnStatuses.size());
    }

}
