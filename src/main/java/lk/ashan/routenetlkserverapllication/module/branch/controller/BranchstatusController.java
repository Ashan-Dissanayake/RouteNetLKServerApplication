package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchstatusDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchStatusService;
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
@RequestMapping(value = "/branchstatuses")
@RequiredArgsConstructor
public class BranchstatusController {

    private final BranchStatusService branchstatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchstatusDto>>> get() {
        List<BranchstatusDto> branchstatuses = branchstatusService.getBranchstatuses();
        return APIResponseBuilder.list(branchstatuses, branchstatuses.size());
    }

}
