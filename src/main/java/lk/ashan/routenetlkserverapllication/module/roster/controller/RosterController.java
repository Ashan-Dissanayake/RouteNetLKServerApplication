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

@CrossOrigin
@RestController
@RequestMapping(value = "/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/summaries")
    public ResponseEntity<APISuccessResponse<List<RosterSummaryDto>>> getRosterSummary() {
        List<RosterSummaryDto> rosterSummaryDtoList = rosterService.getRosterSummary();
        return APIResponseBuilder.list(rosterSummaryDtoList,rosterSummaryDtoList.size());
    }

    @PreAuthorize("hasAuthority('roster-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<RosterSummaryDto>> createRoster(
            @RequestBody @Valid RosterRequestDto rosterRequestDto)
    {
        RosterSummaryDto savedRoster = rosterService.createRoster(rosterRequestDto);
        return APIResponseBuilder.created(savedRoster, savedRoster.getId());
    }

}
