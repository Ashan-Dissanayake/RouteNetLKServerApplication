package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.ShiftService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ShiftDto>>> get() {
        List<ShiftDto> shifts = shiftService.getShifts();
        return APIResponseBuilder.getResponse(shifts, shifts.size());
    }
}
