package lk.ashan.routenetlkserverapllication.module.roster.controller;


import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterShiftService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing roster shifts.
 * Provides endpoints to retrieve roster shift details.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/roster-shifts")
@RequiredArgsConstructor
public class RosterShiftController {

    private final RosterShiftService rosterShiftService;

    /**
     * Retrieves a list of roster shift details for a given roster ID.
     *
     * @param rosterId the ID of the roster to retrieve shifts for
     * @return a ResponseEntity containing a success response with the list of roster shift details
     * @throws IllegalArgumentException if the provided rosterId is invalid
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{rosterId}")
    public ResponseEntity<APISuccessResponse<List<RosterShiftDetailResponseDto>>> getByRosterId(
            @PathVariable Integer rosterId
    ) {
        List<RosterShiftDetailResponseDto> rosterShifts =
                rosterShiftService.getRosterShiftRosterId(rosterId);
        return APIResponseBuilder.list(rosterShifts,rosterShifts.size());
    }

}
