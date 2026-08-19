package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.ShiftSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.ShiftService;
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
 * Controller for managing shift-related operations.
 * Provides endpoints for retrieving shift summaries.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    /**
     * Retrieves a list of shift summaries.
     *
     * @return a ResponseEntity containing an APISuccessResponse with a list of ShiftSummaryDto objects
     * @throws SecurityException if the user is not authenticated
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ShiftSummaryDto>>> get() {
        List<ShiftSummaryDto> shifts = shiftService.getShifts();
        return APIResponseBuilder.list(shifts, shifts.size());
    }

}
