package lk.ashan.routenetlkserverapllication.module.roster.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentScheduler;
import lk.ashan.routenetlkserverapllication.module.roster.planner.RosterAssignmentSolution;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Resolution;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rosters")
@RequiredArgsConstructor
public class RosterController {

    private final RosterService rosterService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RosterDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<RosterDetailResponseDto> rosters = params.isEmpty()
                ?rosterService.getRosters()
                : rosterService.searchRosters(params);

        return APIResponseBuilder.getResponse(rosters, rosters.size());
    }

    @PostMapping
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> add(
            @RequestBody @Valid RosterCreateRequestDto rosterCreateRequestDto)
    {
        RosterDetailResponseDto savedRoster = rosterService.createRoster(rosterCreateRequestDto);
        return APIResponseBuilder.postResponse(savedRoster, savedRoster.getId());
    }

    @PostMapping(value = "/solve")
    public ResponseEntity<APISuccessResponse<RosterAssignmentSolution>> solve(@RequestParam HashMap<String, String> params) {
        String branchId = params.get("branchId");
        String date = params.get("date");

        RosterAssignmentSolution solution = rosterService.solveRoster(branchId,date);
        return APIResponseBuilder.postResponse(solution, solution.getTotalSlots());
    }

}
