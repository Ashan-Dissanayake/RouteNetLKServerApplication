package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.ConditionrateDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.ConditionRateService;
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
@RequestMapping(value = "/condition-rates")
@RequiredArgsConstructor
public class ConditionRateController {

    private final ConditionRateService conditionRateService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path ="/summaries", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<ConditionrateDto>>> get() {
        List<ConditionrateDto> conditionRates = conditionRateService.getConditionRates();
        return APIResponseBuilder.list(conditionRates, conditionRates.size());
    }

}
