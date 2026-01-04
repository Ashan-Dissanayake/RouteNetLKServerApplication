package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rosterstatuses")
@RequiredArgsConstructor
public class RosterStatusController {

    private final RosterStatusService rosterStatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RosterStatusDto>>> get() {
        List<RosterStatusDto> rosterStatuses = rosterStatusService.getRosterStatuses();
        return APIResponseBuilder.getResponse(rosterStatuses, rosterStatuses.size());
    }

}
