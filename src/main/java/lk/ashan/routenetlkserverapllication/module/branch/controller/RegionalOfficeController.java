package lk.ashan.routenetlkserverapllication.module.branch.controller;

import lk.ashan.routenetlkserverapllication.module.branch.dto.RegionalofficeDto;
import lk.ashan.routenetlkserverapllication.module.branch.service.RegionalOfficeService;
import lk.ashan.routenetlkserverapllication.shared.api.dto.APISuccessResponse;
import lk.ashan.routenetlkserverapllication.shared.api.APIResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping(value = "/regionaloffices")
@RequiredArgsConstructor
public class RegionalOfficeController {

    private final RegionalOfficeService regionalofficeService;

    @GetMapping(path ="/list", produces = "application/json")
    public ResponseEntity<APISuccessResponse<List<RegionalofficeDto>>> get() {
        List<RegionalofficeDto> regionalOffices = regionalofficeService.getRegionalOffices();
        return APIResponseBuilder.list(regionalOffices, regionalOffices.size());
    }

}
