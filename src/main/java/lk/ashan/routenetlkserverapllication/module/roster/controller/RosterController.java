package lk.ashan.routenetlkserverapllication.module.roster.controller;

import  jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterSummaryDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing roster-related operations.
 * Provides endpoints for retrieving roster summaries and creating new rosters.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    /**
     * Retrieves a summary of all rosters.
     *
     * @return a ResponseEntity containing a success response with a list of RosterSummaryDto objects.
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/summaries")
    public ResponseEntity<APISuccessResponse<List<RosterSummaryDto>>> getRosterSummary() {
        List<RosterSummaryDto> rosterSummaryDtoList = rosterService.getRosterSummary();
        return APIResponseBuilder.list(rosterSummaryDtoList,rosterSummaryDtoList.size());
    }

    /**
     * Creates a new roster based on the provided request data.
     *
     * @param rosterRequestDto the data for the new roster, validated for correctness.
     * @return a ResponseEntity containing a success response with the created RosterSummaryDto object.
     * @throws jakarta.validation.ConstraintViolationException if the request data is invalid.
     */
    @PreAuthorize("hasAuthority('roster-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<RosterSummaryDto>> createRoster(
            @RequestBody @Valid RosterRequestDto rosterRequestDto)
    {
        RosterSummaryDto savedRoster = rosterService.createRoster(rosterRequestDto);
        return APIResponseBuilder.created(savedRoster, savedRoster.getId());
    }

}
