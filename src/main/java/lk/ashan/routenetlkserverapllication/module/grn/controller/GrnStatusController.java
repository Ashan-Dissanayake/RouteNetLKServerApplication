package lk.ashan.routenetlkserverapllication.module.grn.controller;

import lk.ashan.routenetlkserverapllication.module.grn.model.dto.GrnStatusDto;
import lk.ashan.routenetlkserverapllication.module.grn.service.GrnStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/grn-statuses")
@RequiredArgsConstructor
public class GrnStatusController {

    private final GrnStatusService grnstatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<GrnStatusDto>>> get() {
        List<GrnStatusDto> grnStatuses = grnstatusService.getGrnStatuses();
        return APIResponseBuilder.list(grnStatuses, grnStatuses.size());
    }

}
