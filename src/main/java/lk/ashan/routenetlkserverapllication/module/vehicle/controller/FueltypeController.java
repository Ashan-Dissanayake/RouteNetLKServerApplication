package lk.ashan.routenetlkserverapllication.module.vehicle.controller;

import lk.ashan.routenetlkserverapllication.module.vehicle.model.dto.FueltypeDto;
import lk.ashan.routenetlkserverapllication.module.vehicle.service.FueltypeService;
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
@RequestMapping(value = "/fueltypes")
@RequiredArgsConstructor
public class FueltypeController {

    private final FueltypeService fueltypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<FueltypeDto>>> get() {
        List<FueltypeDto> fueltypes = fueltypeService.getFueltypes();
        return APIResponseBuilder.getResponse(fueltypes, fueltypes.size());
    }

}
