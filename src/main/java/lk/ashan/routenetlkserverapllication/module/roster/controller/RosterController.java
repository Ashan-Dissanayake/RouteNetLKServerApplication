package lk.ashan.routenetlkserverapllication.module.roster.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftRosterAssignmentDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterService;
import lk.ashan.routenetlkserverapllication.module.trip.dto.OverrideSuggestionResponse;
import lk.ashan.routenetlkserverapllication.module.trip.dto.TripDetailResponseDto;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    @GetMapping( produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RosterDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<RosterDetailResponseDto> conductors = params.isEmpty()
                ?rosterService.getRosters()
                : rosterService.searchRosters(params);
        return APIResponseBuilder.list(conductors, conductors.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> createRoster(
            @RequestBody @Valid RosterCreateRequestDto createRequestDto
    ){
        RosterDetailResponseDto savedRoster = rosterService.createRoster(createRequestDto);
        return APIResponseBuilder.created(savedRoster,savedRoster.getId());
    }

    @PostMapping("/{rosterId}/generate-suggestions")
    public ResponseEntity<APISuccessResponse<RosterAssignmentSuggestionResponse>> generateSuggestions(
            @PathVariable Integer rosterId) {
        RosterAssignmentSuggestionResponse response = rosterService.rosterAssigmentSuggestion(rosterId);
        return APIResponseBuilder.ok(
                response,
                Map.of("action", "suggestion_generated")
        );    }

    @PostMapping("/{rosterId}/approve-override")
    public ResponseEntity<APISuccessResponse<ShiftRosterAssignmentDto>> approveOverride(
            @PathVariable Integer rosterId) {

        ShiftRosterAssignmentDto response =
                rosterService.approveSuggestion(rosterId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "override_approved")
        );    }



}
