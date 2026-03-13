package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.model.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchTypeService;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/branch-types")
@RequiredArgsConstructor
public class BranchTypeController {

    private final BranchTypeService branchTypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchtypeDto>>> get() {
        List<BranchtypeDto> branchTypes = branchTypeService.getBranchtypes();
        return APIResponseBuilder.list(branchTypes, branchTypes.size());
    }

}
