package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterGenerationResponse;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDTO;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterShiftAssignmentService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/roster-shift-assignment")
@RequiredArgsConstructor
public class RosterShiftController {

    private final RosterShiftAssignmentService rosterShiftAssignmentService;

    @GetMapping("/view/{rosterId}")
    public ResponseEntity<APISuccessResponse<List<RosterShiftAssignmentResponseDTO>>> getFullRoster
            (@PathVariable Integer rosterId) {
        List<RosterShiftAssignmentResponseDTO> assignments =
                rosterShiftAssignmentService.getAssignmentsByRosterId(rosterId);
        return APIResponseBuilder.list(assignments, assignments.size());
    }

    @PostMapping("/generate/{rosterId}")
    public ResponseEntity<APISuccessResponse<RosterGenerationResponse>> generateRoster(
            @PathVariable Integer rosterId
    ){

        rosterShiftAssignmentService.generateRoster(rosterId);

        RosterGenerationResponse data = new RosterGenerationResponse(
                rosterId,
                "Roster generation task has been queued and is processing in the background.",
                "PROCESSING",
                LocalDateTime.now()
        );
        return APIResponseBuilder.accepted(data);
    }


}
