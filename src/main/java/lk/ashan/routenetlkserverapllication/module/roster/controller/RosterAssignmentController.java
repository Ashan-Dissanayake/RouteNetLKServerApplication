package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.dto.RosterAssignmentDetailedResponseDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.RosterAssignmentService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rosterassignments")
@RequiredArgsConstructor
public class RosterAssignmentController {

    private final RosterAssignmentService rosterAssignmentService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RosterAssignmentDetailedResponseDto>>> get() {
        List<RosterAssignmentDetailedResponseDto> rosterAssignments =  rosterAssignmentService.getRosterAssignments();
        return APIResponseBuilder.getResponse(rosterAssignments, rosterAssignments.size());
    }
}
