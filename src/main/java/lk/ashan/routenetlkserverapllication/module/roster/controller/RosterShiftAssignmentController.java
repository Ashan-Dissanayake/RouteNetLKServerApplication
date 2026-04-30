package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterGenerationResponse;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
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
public class RosterShiftAssignmentController {

    private final RosterShiftAssignmentService rosterShiftAssignmentService;

    @GetMapping("/view/{rosterId}")
    public ResponseEntity<APISuccessResponse<List<RosterShiftAssignmentResponseDto>>> getFullRoster
            (@PathVariable Integer rosterId) {
        List<RosterShiftAssignmentResponseDto> assignments =
                rosterShiftAssignmentService.getAssignmentsByRosterId(rosterId);
        return APIResponseBuilder.list(assignments, assignments.size());
    }

    @PostMapping("/{rosterId}/generate")
    public ResponseEntity<APISuccessResponse<RosterGenerationResponse>> generateRoster(
            @PathVariable Integer rosterId
    ){
        rosterShiftAssignmentService.generateRosterShiftAssignments(rosterId);

        RosterGenerationResponse data = new RosterGenerationResponse(
                rosterId,
                "Roster generation completed successfully.",
                "COMPLETED",
                LocalDateTime.now()
        );
        return APIResponseBuilder.ok(data);
    }

    @PostMapping("/{assignmentId}/approved")
    public ResponseEntity<APISuccessResponse<String>> approveSuggestion(
            @PathVariable Integer assignmentId)
    {   
        rosterShiftAssignmentService.approveSuggestion(assignmentId);
        return APIResponseBuilder.ok("Shift assignment suggestion approved successfully.");
    }

    @PostMapping("/{assignmentId}/cancelled")
    public ResponseEntity<APISuccessResponse<String>> cancelSuggestion(
            @PathVariable Integer assignmentId)
    {
        rosterShiftAssignmentService.cancelSuggestion(assignmentId);
        return APIResponseBuilder.ok("Shift assignment suggestion cancelled successfully.");
    }


}
