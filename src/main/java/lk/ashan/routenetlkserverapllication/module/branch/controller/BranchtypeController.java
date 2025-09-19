package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.dto.BranchtypeDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.BranchtypeService;
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
@RequestMapping(value = "/branchtypes")
@RequiredArgsConstructor
public class BranchtypeController {

    private final BranchtypeService branchtypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<BranchtypeDto>>> get() {
        List<BranchtypeDto> branchtypes = branchtypeService.getBranchtypes();
        return APIResponseBuilder.getResponse(branchtypes, branchtypes.size());
    }

}
