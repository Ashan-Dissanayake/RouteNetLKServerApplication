package lk.ashan.routenetlkserverapllication.module.permit.controller;

import lk.ashan.routenetlkserverapllication.module.permit.model.dto.PermitStatusDto;
import lk.ashan.routenetlkserverapllication.module.permit.service.PermitStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/permit-statuses")
@RequiredArgsConstructor
public class PermitStatusController {

    private final PermitStatusService permitstatusService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<PermitStatusDto>>> get() {
        List<PermitStatusDto> permitStatuses = permitstatusService.getPermitStatuses();
        return APIResponseBuilder.list(permitStatuses, permitStatuses.size());
    }

}
