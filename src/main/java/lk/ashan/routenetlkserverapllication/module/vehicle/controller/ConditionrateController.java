package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.ConditionrateService;
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
@RequestMapping(value = "/conditionrates")
@RequiredArgsConstructor
public class ConditionrateController {

    private final ConditionrateService conditionrateService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConditionrateDto>>> get() {
        List<ConditionrateDto> conditionrates = conditionrateService.getConditionRates();
        return APIResponseBuilder.getResponse(conditionrates, conditionrates.size());
    }

}
