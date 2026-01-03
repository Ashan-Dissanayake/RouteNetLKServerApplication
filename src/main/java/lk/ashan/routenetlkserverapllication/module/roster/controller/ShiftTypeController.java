package lk.ashan.routenetlkserverapllication.module.roster.controller;

import lk.ashan.routenetlkserverapllication.module.roster.dto.ShiftTypeDto;
import lk.ashan.routenetlkserverapllication.module.roster.service.ShiftTypeService;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shifttypes")
@RequiredArgsConstructor
public class ShiftTypeController {

    private final ShiftTypeService shiftTypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ShiftTypeDto>>> get() {
        List<ShiftTypeDto> shiftTypes = shiftTypeService.getShiftTypes();
        return APIResponseBuilder.getResponse(shiftTypes, shiftTypes.size());
    }

}
