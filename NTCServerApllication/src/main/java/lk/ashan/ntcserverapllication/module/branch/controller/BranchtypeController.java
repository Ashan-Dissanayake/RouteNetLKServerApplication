package lk.ashan.ntcserverapllication.module.branch.controller;

import lk.ashan.ntcserverapllication.module.branch.dto.BranchtypeResponse;
import lk.ashan.ntcserverapllication.module.branch.service.BranchtypeService;
import lk.ashan.ntcserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.ntcserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/branchtypes")
@RequiredArgsConstructor
public class BranchtypeController {

    private final BranchtypeService branchtypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchtypeResponse>>> get() {
        List<BranchtypeResponse> branchtypes = branchtypeService.getBranchtypes();
        return APIResponseBuilder.getResponse(branchtypes, branchtypes.size());
    }

}
