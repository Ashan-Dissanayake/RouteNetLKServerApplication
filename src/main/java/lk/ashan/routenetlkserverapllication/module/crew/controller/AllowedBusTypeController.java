package lk.ashan.routenetlkserverapllication.module.crew.controller;

import lk.ashan.routenetlkserverapllication.module.crew.dto.AllowedBusTypeDto;
import lk.ashan.routenetlkserverapllication.module.crew.service.AllowedBusTypeService;
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
@RequestMapping(value = "/allowedbustypes")
@RequiredArgsConstructor
public class AllowedBusTypeController {

    private final AllowedBusTypeService allowedBusTypeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<AllowedBusTypeDto>>> get() {
        List<AllowedBusTypeDto>allowedBusTypes =allowedBusTypeService.getAllowedBusTypes();
        return APIResponseBuilder.getResponse(allowedBusTypes,allowedBusTypes.size());
    }

}
