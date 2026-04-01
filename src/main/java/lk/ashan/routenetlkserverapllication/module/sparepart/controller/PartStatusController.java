package lk.ashan.routenetlkserverapllication.module.sparepart.controller;

import lk.ashan.routenetlkserverapllication.module.sparepart.model.dto.PartStatusDto;
import lk.ashan.routenetlkserverapllication.module.sparepart.service.PartStatusService;
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
@RequestMapping(value = "/part-statuses")
@RequiredArgsConstructor
public class PartStatusController {

    private final PartStatusService partstatusService;

    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PartStatusDto>>> get() {
        List<PartStatusDto> partStatuses = partstatusService.getPartStatuses();
        return APIResponseBuilder.list(partStatuses, partStatuses.size());
    }

}
