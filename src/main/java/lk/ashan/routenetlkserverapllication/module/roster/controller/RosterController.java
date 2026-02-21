package lk.ashan.routenetlkserverapllication.module.roster.controller;

import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.roster.dto.*;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.exception.BusinessRuleViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/rosters")
@RequiredArgsConstructor
@Slf4j
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

    /**
     * Lock roster (DRAFT → LOCKED)
     * POST /rosters/{rosterId}/lock
     */
    @PostMapping("/{rosterId}/lock")
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> lockRoster(
            @PathVariable Integer rosterId) {

        log.info("Locking roster {}", rosterId);

        RosterDetailResponseDto lockedRoster = rosterService.lockRoster(rosterId);

        return APIResponseBuilder.ok(
                lockedRoster,
                Map.of(
                        "action", "locked",
                        "message", "Roster locked. Employees can now confirm assignments."
                )
        );
    }

    /**
     * Unlock roster (LOCKED → DRAFT)
     * POST /rosters/{rosterId}/unlock
     */
    @PostMapping("/{rosterId}/unlock")
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> unlockRoster(
            @PathVariable Integer rosterId) {

        log.info("Unlocking roster {}", rosterId);

        RosterDetailResponseDto unlockedRoster = rosterService.unlockRoster(rosterId);

        return APIResponseBuilder.ok(
                unlockedRoster,
                Map.of(
                        "action", "unlocked",
                        "message", "Roster unlocked. Confirmations reset to suggestions."
                )
        );
    }

    /**
     * Archive roster (LOCKED → ARCHIVED)
     * POST /rosters/{rosterId}/archive
     */
    @PostMapping("/{rosterId}/archive")
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> archiveRoster(
            @PathVariable Integer rosterId) {

        log.info("Archiving roster {}", rosterId);

        RosterDetailResponseDto archivedRoster = rosterService.archiveRoster(rosterId);

        return APIResponseBuilder.ok(
                archivedRoster,
                Map.of(
                        "action", "archived",
                        "message", "Roster archived. No further changes allowed."
                )
        );
    }

    /**
     * Generate optimized assignment suggestions using AI
     * POST /rosters/{rosterId}/generate-suggestions
     */
    @PostMapping("/{rosterId}/generate-suggestions")
    public ResponseEntity<APISuccessResponse<RosterAssignmentSuggestionResponse>> generateSuggestions(
            @PathVariable Integer rosterId) {

        log.info("Generating AI suggestions for roster {}", rosterId);

        RosterAssignmentSuggestionResponse response =
                rosterService.rosterAssignmentSuggestion(rosterId);

        String message = String.format(
                "Generated %d assignment suggestions. %s",
                response.getAssignmentsFilled(),
                response.getAssignmentsUnfilled() > 0
                        ? response.getAssignmentsUnfilled() + " positions could not be filled."
                        : "All positions filled!"
        );

        return APIResponseBuilder.ok(
                response,
                Map.of(
                        "action", "suggestions_generated",
                        "message", message
                )
        );
    }

    @PostMapping("/{rosterId}/approve-suggestion")
    public ResponseEntity<APISuccessResponse<ShiftRosterAssignmentDto>> approveOverride(
            @PathVariable Integer rosterId) {

        ShiftRosterAssignmentDto response =
                rosterService.approveSuggestion(rosterId);

        return APIResponseBuilder.ok(
                response,
                Map.of("action", "suggestion_approved")
        );    }

    /**
     * Get all suggested assignments for a roster
     * GET /rosters/{rosterId}/suggestions
     */
    @GetMapping("/{rosterId}/suggestions")
    public ResponseEntity<APISuccessResponse<List<RosterAssignmentSuggestionResponse>>> getSuggestions(
            @PathVariable Integer rosterId) {

        log.info("Fetching suggestions for roster {}", rosterId);

        List<RosterAssignmentSuggestionResponse> suggestions =
                rosterService.getSuggestions(rosterId);

        return APIResponseBuilder.list(suggestions, suggestions.size());
    }


    /**
     * Clear all suggestions for a roster
     * DELETE /rosters/{rosterId}/suggestions
     */
    @DeleteMapping("/{rosterId}/suggestions")
    public ResponseEntity<APISuccessResponse<Void>> clearAllSuggestions(
            @PathVariable Integer rosterId) {

        log.info("Clearing all suggestions for roster {}", rosterId);

        rosterService.clearAllSuggestions(rosterId);

        return APIResponseBuilder.ok(
                null,
                Map.of(
                        "action", "suggestions_cleared",
                        "message", "All suggestions cleared successfully"
                )
        );
    }


    /**
     * Approve a suggested assignment (keep it)
     * POST /rosters/assignments/{assignmentId}/approve
     */
    @PostMapping("/assignments/{assignmentId}/approve")
    public ResponseEntity<APISuccessResponse<ShiftRosterAssignmentDto>> approveSuggestion(
            @PathVariable Integer assignmentId) {

        log.info("Approving suggestion {}", assignmentId);

        ShiftRosterAssignmentDto approved =
                rosterService.approveSuggestion(assignmentId);

        return APIResponseBuilder.ok(
                approved,
                Map.of(
                        "action", "suggestion_approved",
                        "message", "Suggestion approved successfully"
                )
        );
    }

    /**
     * Reject a suggested assignment (delete it)
     * DELETE /rosters/assignments/{assignmentId}/reject
     */
    @DeleteMapping("/assignments/{assignmentId}/reject")
    public ResponseEntity<APISuccessResponse<Void>> rejectSuggestion(
            @PathVariable Integer assignmentId) {

        log.info("Rejecting suggestion {}", assignmentId);

        rosterService.rejectSuggestion(assignmentId);

        return APIResponseBuilder.ok(
                null,
                Map.of(
                        "action", "suggestion_rejected",
                        "message", "Suggestion rejected successfully"
                )
        );
    }


    /**
     * Update existing roster (DRAFT only)
     * PUT /rosters/{rosterId}
     */
    @PutMapping("/{rosterId}")
    public ResponseEntity<APISuccessResponse<RosterDetailResponseDto>> updateRoster(
            @PathVariable Integer rosterId,
            @RequestBody @Valid RosterUpdateRequestDto updateRequestDto) {

        log.info("Updating roster {}", rosterId);

        // Ensure path variable matches DTO
        if (!rosterId.equals(updateRequestDto.getId())) {
            throw new BusinessRuleViolationException(
                    "Path variable rosterId (" + rosterId + ") does not match request body ID (" +
                            updateRequestDto.getId() + ")"
            );
        }

        RosterDetailResponseDto updatedRoster = rosterService.updateRoster(updateRequestDto);

        return APIResponseBuilder.ok(
                updatedRoster,
                Map.of(
                        "action", "updated",
                        "message", "Roster updated successfully"
                )
        );
    }

    /**
     * Soft delete roster (DRAFT only)
     * DELETE /rosters/{rosterId}
     */
    @DeleteMapping("/{rosterId}")
    public ResponseEntity<APISuccessResponse<Void>> deleteRoster(
            @PathVariable Integer rosterId) {

        log.info("Deleting roster {}", rosterId);

        rosterService.deleteRoster(rosterId);

        return APIResponseBuilder.ok(
                null,
                Map.of(
                        "action", "deleted",
                        "message", "Roster deleted successfully"
                )
        );
    }

    /**
     * Regenerate suggestions (clear old + generate new)
     * POST /rosters/{rosterId}/regenerate-suggestions
     */
    @PostMapping("/{rosterId}/regenerate-suggestions")
    public ResponseEntity<APISuccessResponse<RosterAssignmentSuggestionResponse>> regenerateSuggestions(
            @PathVariable Integer rosterId) {

        log.info("Regenerating suggestions for roster {}", rosterId);

        RosterAssignmentSuggestionResponse response =
                rosterService.regenerateSuggestions(rosterId);

        String message = String.format(
                "Re-generated %d assignment suggestions. %s",
                response.getAssignmentsFilled(),
                response.getAssignmentsUnfilled() > 0
                        ? response.getAssignmentsUnfilled() + " positions could not be filled."
                        : "All positions filled!"
        );

        return APIResponseBuilder.ok(
                response,
                Map.of(
                        "action", "suggestions_regenerated",
                        "message", message
                )
        );
    }

}
