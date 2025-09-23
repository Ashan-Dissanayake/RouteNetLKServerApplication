package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchDetailResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchSummaryResponseDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchCreateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchUpdateRequestDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchService;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchDetailResponseDto>>> get(
            @RequestParam HashMap<String, String> params
    ) {
        List<BranchDetailResponseDto> branches = params.isEmpty()
                ? branchService.getBranches()
                : branchService.searchBranch(params);

        return APIResponseBuilder.getResponse(branches, branches.size());
    }


    @PostMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> add(@RequestBody BranchCreateRequestDto branchCreateRequest) {
        BranchDetailResponseDto savedBranch = branchService.createBranch(branchCreateRequest);
        return APIResponseBuilder.postResponse(savedBranch, savedBranch.getId());
    }

    @PutMapping
    public ResponseEntity<APISuccessResponse<BranchDetailResponseDto>> update(@RequestBody BranchUpdateRequestDto branchUpdateRequest) {
        BranchDetailResponseDto updatedBranch = branchService.updateBranch(branchUpdateRequest);
        return APIResponseBuilder.putResponse(updatedBranch, updatedBranch.getId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APISuccessResponse<BranchSummaryResponseDto>> delete(@PathVariable Integer id) {
        branchService.deleteBranch(id);
        return APIResponseBuilder.deleteResponse(id);
    }

}
