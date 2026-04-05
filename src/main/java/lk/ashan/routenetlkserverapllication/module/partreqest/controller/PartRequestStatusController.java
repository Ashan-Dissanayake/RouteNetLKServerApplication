package lk.ashan.routenetlkserverapllication.module.partreqest.controller;

import lk.ashan.routenetlkserverapllication.module.partreqest.model.dto.PartRequestStatusDto;
import lk.ashan.routenetlkserverapllication.module.partreqest.service.PartRequestStatusService;
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
@RequestMapping(value = "/part-request-statuses")
@RequiredArgsConstructor
public class PartRequestStatusController {

    private final PartRequestStatusService partRequeststatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartRequestStatusDto>>> get() {
        List<PartRequestStatusDto> partRequestStatuses = partRequeststatusService.getPartRequestStatuses();
        return APIResponseBuilder.list(partRequestStatuses, partRequestStatuses.size());
    }

}
