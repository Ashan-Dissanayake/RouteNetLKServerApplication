package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterGenerationResponse;
import lk.ashan.routenetlkserverapllication.module.roster.model.dto.RosterShiftAssignmentResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterShiftAssignmentService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller for managing roster shift assignments.
 * Provides endpoints for viewing, generating, approving, and cancelling roster shift assignments.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/roster-shift-assignment")
@RequiredArgsConstructor
public class RosterShiftAssignmentController {

    private final RosterShiftAssignmentService rosterShiftAssignmentService;

    /**
     * Retrieves the full roster shift assignments for a given roster ID.
     *
     * @param rosterId the ID of the roster to retrieve assignments for
     * @return a response entity containing a list of roster shift assignment DTOs
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('roster-shift-assignment-view')")
    @GetMapping("/view/{rosterId}")
    public ResponseEntity<APISuccessResponse<List<RosterShiftAssignmentResponseDto>>> getFullRoster(
            @PathVariable Integer rosterId
    ) {
        List<RosterShiftAssignmentResponseDto> assignments =
                rosterShiftAssignmentService.getAssignmentsByRosterId(rosterId);
        return APIResponseBuilder.list(assignments, assignments.size());
    }

    /**
     * Generates roster shift assignments for a given roster ID.
     *
     * @param rosterId the ID of the roster to generate assignments for
     * @return a response entity containing the roster generation response
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('roster-shift-assignment-generate')")
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

    /**
     * Approves a shift assignment suggestion for a given assignment ID.
     *
     * @param assignmentId the ID of the assignment to approve
     * @return a response entity containing a success message
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('roster-shift-assignment-approved')")
    @PostMapping("/{assignmentId}/approved")
    public ResponseEntity<APISuccessResponse<String>> approveSuggestion(
            @PathVariable Integer assignmentId)
    {
        rosterShiftAssignmentService.approveSuggestion(assignmentId);
        return APIResponseBuilder.ok("Shift assignment suggestion approved successfully.");
    }

    /**
     * Cancels a shift assignment suggestion for a given assignment ID.
     *
     * @param assignmentId the ID of the assignment to cancel
     * @return a response entity containing a success message
     * @throws org.springframework.security.access.AccessDeniedException if the user does not have the required authority
     */
    @PreAuthorize("hasAuthority('roster-shift-assignment-cancelled')")
    @PostMapping("/{assignmentId}/cancelled")
    public ResponseEntity<APISuccessResponse<String>> cancelSuggestion(
            @PathVariable Integer assignmentId)
    {
        rosterShiftAssignmentService.cancelSuggestion(assignmentId);
        return APIResponseBuilder.ok("Shift assignment suggestion cancelled successfully.");
    }
}
