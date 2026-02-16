package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.dto.RouteFamiliarityLevelDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.RouteFamiliarityLevelService;
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
@RequestMapping(value = "/routefamiliaritylevels")
@RequiredArgsConstructor
public class RouteFamiliarityLevelController {

    private final RouteFamiliarityLevelService routeFamiliarityLevelService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RouteFamiliarityLevelDto>>> get() {
        List<RouteFamiliarityLevelDto> routeFamiliarityLevels = routeFamiliarityLevelService.getRouteFamiliarityLevels();
        return APIResponseBuilder.list(routeFamiliarityLevels, routeFamiliarityLevels.size());
    }

}
