package lk.ashan.routenetlkserverapllication.module.branch.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchSummaryDto;
import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing branch-related operations.
 * Provides endpoints for retrieving, creating, updating, and deactivating branches.
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/branches")
@RequiredArgsConstructor
@Tag(name = "Branch Management", description = "Endpoints for managing branches in the system.")
public class BranchController {

    private final BranchService branchService;

    /**
     * Retrieves a list of branch details. If query parameters are provided, performs a search.
     *
     * @param params A map of query parameters for filtering branches.
     * @return A response entity containing a list of branch details and the total count.
     */
    @Operation(summary = "Get all branches or search by parameters", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved branches"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'branch-view' authority")
    })
    @PreAuthorize("hasAuthority('branch-view')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<BranchDetailResponseDto> branches = params.isEmpty()
                ? branchService.getBranches()
                : branchService.searchBranch(params);

        return APIResponseBuilder.list(branches, branches.size());
    }

    /**
     * Retrieves a summary list of branches.
     *
     * @return A response entity containing a list of branch summaries and the total count.
     */
    @Operation(summary = "Get branch summaries for dropdowns", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved summaries"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchSummaryDto>>> get() {
        List<BranchSummaryDto> branches = branchService.getSummaryBranches();
        return APIResponseBuilder.list(branches, branches.size());
    }

    /**
     * Creates a new branch.
     *
     * @param branchCreateRequest The request body containing branch creation details.
     * @return A response entity containing the created branch details and its ID.
     */
    @Operation(summary = "Add a new branch", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Branch created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'branch-add' authority")
    })
    @PreAuthorize("hasAuthority('branch-add')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> create(
            @RequestBody @Valid BranchCreateRequestDto branchCreateRequest) {
        BranchDetailResponseDto savedBranch = branchService.createBranch(branchCreateRequest);
        return APIResponseBuilder.created(savedBranch, savedBranch.getId());
    }

    /**
     * Updates an existing branch.
     *
     * @param branchUpdateRequest The request body containing branch update details.
     * @return A response entity containing the updated branch details and its ID.
     */
    @Operation(summary = "Update an existing branch", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branch updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'branch-update' authority"),
            @ApiResponse(responseCode = "404", description = "Branch not found")
    })
    @PreAuthorize("hasAuthority('branch-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> update(
            @RequestBody @Valid BranchUpdateRequestDto branchUpdateRequest) {
        BranchDetailResponseDto updatedBranch = branchService.updateBranch(branchUpdateRequest);
        return APIResponseBuilder.updated(updatedBranch, updatedBranch.getId());
    }

    /**
     * Deactivates a list of branches by their IDs.
     *
     * @param ids A list of branch IDs to deactivate.
     * @return A response entity containing the list of deactivated IDs and additional metadata.
     */
    @Operation(summary = "Deactivate multiple branches", 
               security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Branches deactivated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Requires 'branch-delete' authority")
    })
    @PreAuthorize("hasAuthority('branch-delete')")
    @DeleteMapping
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(@RequestBody List<Integer> ids) {
        List<Integer> deactivatedIds = branchService.deactivateBranches(ids);
        return APIResponseBuilder.ok(
                deactivatedIds,
                Map.of("status", "deactivated", "count", deactivatedIds.size())
        );
    }

}
