package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftStatusDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.ShiftStatusService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shiftstatuses")
@RequiredArgsConstructor
public class ShiftStatusController {

    private final ShiftStatusService shiftStatusService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ShiftStatusDto>>> get() {
        List<ShiftStatusDto> shiftStatuses = shiftStatusService.getShiftStatuses();
        return APIResponseBuilder.getResponse(shiftStatuses, shiftStatuses.size());
    }

}
