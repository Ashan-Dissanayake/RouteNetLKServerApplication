package lk.ashan.ntcserverapllication.module.branch.controller;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchstatusResponse;
import lk.ashan.ntcserverapllication.module.branch.service.BranchstatusService;
import lk.ashan.ntcserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.ntcserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/branchstatuses")
@RequiredArgsConstructor
public class BranchstatusController {

    private final BranchstatusService branchstatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchstatusResponse>>> get() {
        List<BranchstatusResponse> branchstatuses = branchstatusService.getBranchstatuses();
        return APIResponseBuilder.getResponse(branchstatuses, branchstatuses.size());
    }

}
