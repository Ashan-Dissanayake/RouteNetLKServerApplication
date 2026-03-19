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

@CrossOrigin
@RestController
@RequestMapping(value = "/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    //@PreAuthorize("hasAuthority('branch-select')")
    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<BranchDetailResponseDto> branches = params.isEmpty()
                ? branchService.getBranches()
                : branchService.searchBranch(params);

        return APIResponseBuilder.list(branches, branches.size());
    }

    //@PreAuthorize("hasAuthority('branch-select')")
    @GetMapping(value = "/list",produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchSummaryDto>>> get() {
        List<BranchSummaryDto> branches =  branchService.getSummaryBranches();
        return APIResponseBuilder.list(branches, branches.size());
    }

    //@PreAuthorize("hasAuthority('branch-insert')")
    @PostMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> create(
            @RequestBody @Valid BranchCreateRequestDto branchCreateRequest) {
        BranchDetailResponseDto savedBranch = branchService.createBranch(branchCreateRequest);
        return APIResponseBuilder.created(savedBranch, savedBranch.getId());
    }

    //@PreAuthorize("hasAuthority('branch-update')")
    @PutMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> update(
            @RequestBody @Valid BranchUpdateRequestDto branchUpdateRequest) {
        BranchDetailResponseDto updatedBranch = branchService.updateBranch(branchUpdateRequest);
        return APIResponseBuilder.updated(updatedBranch, updatedBranch.getId());
    }

    //@PreAuthorize("hasAuthority('branch-delete')")
    @PostMapping("/deactivate")
    public ResponseEntity<APISuccessResponse<List<Integer>>> deactivateBranches(@RequestBody List<Integer> ids) {
        List<Integer> deactivatedIds = branchService.deactivateBranches(ids);
        return APIResponseBuilder.ok(
                deactivatedIds,
                Map.of("status", "deactivated", "count", deactivatedIds.size())
        );
    }

}
