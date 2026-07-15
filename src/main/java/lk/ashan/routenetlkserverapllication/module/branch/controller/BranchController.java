package lk.ashan.routenetlkserverapllication.module.branch.controller;

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
public class BranchController {

    private final BranchService branchService;

    /**
     * Retrieves a list of branch details. If query parameters are provided, performs a search.
     *
     * @param params A map of query parameters for filtering branches.
     * @return A response entity containing a list of branch details and the total count.
     */
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
